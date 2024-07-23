package com.project.food_ordering_service.global.utils.sse;

import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication,
            @RequestHeader(value = "LastEventID", required = false, defaultValue = "")
            String lastEventId
    ) {
        return ResponseEntity.ok(sseService.connect(jwtAuthentication.getId(), lastEventId));
    }

}
