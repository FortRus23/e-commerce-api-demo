package ru.sakhapov.e_commerce_api.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sakhapov.e_commerce_api.api.dto.order.OrderDto;
import ru.sakhapov.e_commerce_api.store.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/orders")
public class OrderController {

    OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders() {
        List<OrderDto> orders = orderService.getOrdersForCurrentUser();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/from-cart")
    public ResponseEntity<OrderDto> createOrderFromCart() {
        OrderDto orderDto = orderService.createOrderFromCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<OrderDto> payOrder(@PathVariable Long id) {
        OrderDto dto = orderService.payOrder(id);
        return ResponseEntity.ok(dto);
    }
}
