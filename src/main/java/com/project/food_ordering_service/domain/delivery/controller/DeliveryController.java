package com.project.food_ordering_service.domain.delivery.controller;

import com.project.food_ordering_service.domain.delivery.dto.DeliveryRequest;
import com.project.food_ordering_service.domain.delivery.dto.DeliveryResponse;
import com.project.food_ordering_service.domain.delivery.dto.DeliveryStatusRequest;
import com.project.food_ordering_service.domain.delivery.entity.Delivery;
import com.project.food_ordering_service.domain.delivery.service.DeliveryService;
import com.project.food_ordering_service.global.utils.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
        DeliveryResponse deliveryResponse = DeliveryResponse.from(delivery);

        return ResponseEntity.ok(deliveryResponse);
    }

    @PatchMapping("/{deliveryId}/state")
    public ResponseEntity<DeliveryResponse> updatedDeliveryStatus(
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication,
            @PathVariable Long deliveryId,
            @RequestBody DeliveryStatusRequest deliveryStatusRequest) {

        Delivery delivery = deliveryService.updateDeliveryStatus(deliveryId, deliveryStatusRequest.getStatus(), jwtAuthentication);

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
