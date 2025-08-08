package ru.sakhapov.e_commerce_api.api.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sakhapov.e_commerce_api.api.dto.order.OrderDto;
import ru.sakhapov.e_commerce_api.store.entity.Order;

@Component
@RequiredArgsConstructor
public class OrderDtoFactory {

    private final OrderItemDtoFactory itemDtoFactory;

    public OrderDto makeOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .createdAt(order.getCreatedAt())
                .orderStatus(order.getOrderStatus().name())
                .orderPrice(order.getOrderPrice())
                .orderItems(
                        order.getOrderItems().stream()
                                .map(itemDtoFactory::makeOrderItemDto)
                                .toList())
                .build();
    }
}
