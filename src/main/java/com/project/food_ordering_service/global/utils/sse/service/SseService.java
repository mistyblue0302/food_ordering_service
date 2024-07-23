package com.project.food_ordering_service.global.utils.sse.service;

import com.project.food_ordering_service.global.utils.sse.exception.SseServerError;
import com.project.food_ordering_service.global.utils.sse.repository.SseRepository;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseService {

    private static final long TIMEOUT = 6000 * 1000;
    private static final String DELIVERY_ALARM = "alarm";

    private final SseRepository sseRepository;

    public SseEmitter connect(Long userId, String lastEventId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        String id = userId + "_" + System.currentTimeMillis();

        sendToClient(emitter, id, "connected");

        sseRepository.save(id, emitter);

        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            sseRepository.delete(id);
        });

        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            sseRepository.delete(id);
        });

        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = sseRepository.findAllEventCacheStartWithId(
                    String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    public void sand(Long userId) {
        String id = String.valueOf(userId);

        Map<String, SseEmitter> sseEmitters = sseRepository.findAllEmitterStartWithById(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    sseRepository.saveEventCache(key, "notification");
                    sendToClient(emitter, key, "null");
                }
        );
    }

    private static void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name(DELIVERY_ALARM)
                    .data(data));
        } catch (IOException e) {
            throw new SseServerError("서버연결 오류", e);
        }
    }
}
