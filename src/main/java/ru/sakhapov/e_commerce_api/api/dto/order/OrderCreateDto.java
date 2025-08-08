package ru.sakhapov.e_commerce_api.api.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {

    List<OrderItemCreateDto> items;
}
