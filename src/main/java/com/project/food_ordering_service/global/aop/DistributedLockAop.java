package com.project.food_ordering_service.global.aop;

import com.project.food_ordering_service.global.annotaion.DistributedLock;
import com.project.food_ordering_service.global.distributedLock.AopTransactionExecutor;
import com.project.food_ordering_service.global.distributedLock.DynamicValueParser;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

    private final RedissonClient redissonClient;
    private final AopTransactionExecutor aopTransactionExecutor;

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    @Around("@annotation(com.project.food_ordering_service.global.annotaion.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = buildLockKey(distributedLock, joinPoint);
        RLock rLock = redissonClient.getLock(key);

        try {
            boolean available = rLock.tryLock(distributedLock.waitTime(),
                    distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!available) {
                return false;
            }
            return aopTransactionExecutor.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            rLock.unlock();
        }
    }

    private String buildLockKey(DistributedLock distributedLock, ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object dynamicKey = DynamicValueParser.getDynamicValue(methodSignature.getParameterNames(),
                joinPoint.getArgs(), distributedLock.key());
        return REDISSON_LOCK_PREFIX + dynamicKey;
    }
}
