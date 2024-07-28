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

    private static final long TIMEOUT = 6000 * 10000;
    private static final String DELIVERY_ALARM = "alarm";

    private final SseRepository sseRepository;

    public SseEmitter connect(Long userId, String lastEventId) {
        String id = makeTimeIncludeId(userId);

        SseEmitter emitter = sseRepository.save(id, new SseEmitter(TIMEOUT));
        log.info("Emitter connected with ID: {} for user ID: {}", id, userId);

        emitter.onCompletion(() -> {
            log.info("onCompletion callback for ID: {}", id);
            sseRepository.delete(id);
        });

        emitter.onTimeout(() -> {
            log.info("onTimeout callback for ID: {}", id);
            sseRepository.delete(id);
        });

        sendToClient(emitter, id, "connected");

        if (lastEventId != null && !lastEventId.isEmpty()) {
            sendLostData(userId, lastEventId, emitter);
        }

        return emitter;
    }


    public void sand(Long userId, Object data) {
        String id = String.valueOf(userId);
        Map<String, SseEmitter> sseEmitters = sseRepository.findAllEmitterStartWithById(id);
        if (sseEmitters.isEmpty()) {
            log.warn("No emitters found for user ID: " + userId);
        }

        sseEmitters.forEach(
                (key, emitter) -> {
                    try {
                        sseRepository.saveEventCache(key, data);
                        sendToClient(emitter, key, data);
                    } catch (Exception e) {
                        sseRepository.delete(id);
                        throw new SseServerError("sand", e);
                    }
                }
        );
    }

    private static String makeTimeIncludeId(Long userId) {
        String id = userId + "_" + System.currentTimeMillis();
        return id;
    }


    private void sendLostData(Long userId, String lastEventId, SseEmitter emitter) {
        Map<String, Object> events = sseRepository.findAllEventCacheStartWithId(
                String.valueOf(userId));
        events.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
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
