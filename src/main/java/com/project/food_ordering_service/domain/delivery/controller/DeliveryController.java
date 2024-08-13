package com.project.food_ordering_service.domain.delivery.controller;

import com.project.food_ordering_service.domain.delivery.dto.DeliveryRequest;
import com.project.food_ordering_service.domain.delivery.dto.DeliveryResponse;
import com.project.food_ordering_service.domain.delivery.dto.DeliveryStatusRequest;
import com.project.food_ordering_service.domain.delivery.entity.Delivery;
import com.project.food_ordering_service.domain.delivery.service.DeliveryService;
import com.project.food_ordering_service.domain.user.entity.Role;
import com.project.food_ordering_service.global.annotaion.CheckRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @CheckRole(requiredRole = Role.RIDER)
    @PostMapping("/assign")
    public ResponseEntity<DeliveryResponse> assignDelivery(
            @RequestBody DeliveryRequest deliveryRequest) {
        Delivery delivery = deliveryService.assignDelivery(deliveryRequest.getOrderId(),
                deliveryRequest.getRiderId());
        DeliveryResponse deliveryResponse = DeliveryResponse.from(delivery);

        return ResponseEntity.ok(deliveryResponse);
    }

    @CheckRole(requiredRole = Role.RIDER)
    @PatchMapping("/{deliveryId}/state")
    public ResponseEntity<DeliveryResponse> updatedDeliveryStatus(
            @PathVariable Long deliveryId,
            @RequestBody DeliveryStatusRequest deliveryStatusRequest) {
        Delivery delivery = deliveryService.updateDeliveryStatus(deliveryId,
                deliveryStatusRequest.getStatus());

        return ResponseEntity.ok(DeliveryResponse.from(delivery));
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Void> deleteDelivery(
            @PathVariable Long deliveryId) {
        deliveryService.cancelDelivery(deliveryId);
        return ResponseEntity.noContent().build();
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
