package com.project.food_ordering_service.global.utils.sse.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
@Slf4j
@RequiredArgsConstructor
public class SseRepository {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();


    public SseEmitter save(String id, SseEmitter emitter) {

        log.info("Set Emitter to Redis {}({})", id, emitter);
        log.info("emitter list size: {}", emitterMap.size());
        emitterMap.put(id, emitter);
        return emitter;

    }

    public void saveEventCache(String emitterId, Object event) {
        eventCache.put(emitterId, event);
    }

    public Map<String, SseEmitter> findAllEmitterStartWithById(String id) {
        return emitterMap.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }


    public void delete(String userId) {
        emitterMap.remove(userId);
    }


    public Map<String, Object> findAllEventCacheStartWithId(String userId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
