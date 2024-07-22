package com.project.food_ordering_service.global.utils.sse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class SseRepository {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

}
