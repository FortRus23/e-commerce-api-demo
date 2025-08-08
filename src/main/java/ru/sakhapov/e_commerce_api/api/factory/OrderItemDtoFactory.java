package ru.sakhapov.e_commerce_api.api.factory;

import org.springframework.stereotype.Component;
import ru.sakhapov.e_commerce_api.api.dto.order.OrderItemDto;
import ru.sakhapov.e_commerce_api.store.entity.OrderItem;

@Component
public class OrderItemDtoFactory {

    public OrderItemDto makeOrderItemDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }

}


