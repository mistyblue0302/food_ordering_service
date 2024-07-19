package com.project.food_ordering_service.domain.delivery.controller;

import com.project.food_ordering_service.domain.delivery.dto.DeliveryRequest;
import com.project.food_ordering_service.domain.delivery.dto.DeliveryResponse;
import com.project.food_ordering_service.domain.delivery.dto.DeliveryStatusRequest;
import com.project.food_ordering_service.domain.delivery.entity.Delivery;
import com.project.food_ordering_service.domain.delivery.service.DeliveryService;
import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/{orderId}/assign")
    public ResponseEntity<DeliveryResponse> assignDelivery(
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication,
            @PathVariable Long orderId,
            @RequestBody DeliveryRequest deliveryRequest) {

        Long riderId = jwtAuthentication.getId();
        Delivery delivery = deliveryService.assignDelivery(orderId, riderId, jwtAuthentication);

        return ResponseEntity.ok(DeliveryResponse.from(delivery));
    }

    @PatchMapping("/{deliveryId}/status")
    public ResponseEntity<DeliveryResponse> updatedDeliveryStatus(
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication,
            @PathVariable Long deliveryId,
            @RequestBody DeliveryStatusRequest deliveryStatusRequest) {

        if (!jwtAuthentication.getRole().equals(Role.RIDER)) {
            throw new AccessDeniedException("배달원만 배달 상태를 업데이트할 수 있습니다.");
        }

        Delivery delivery = deliveryService.updateDeliveryStatus(deliveryId, deliveryStatusRequest.getStatus());

        return ResponseEntity.ok(DeliveryResponse.from(delivery));
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponse> getDeliveryById(
            @PathVariable Long deliveryId) {

        Delivery delivery = deliveryService.getDeliveryById(deliveryId);
        return ResponseEntity.ok(DeliveryResponse.from(delivery));
    }

    @GetMapping
    public ResponseEntity<Page<DeliveryResponse>> getDeliveries(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DeliveryResponse> deliveryResponses = deliveryService.getDeliveries(pageable);
        return ResponseEntity.ok(deliveryResponses);
    }
}
