package ru.sakhapov.e_commerce_api.api.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class OrderDto {

    @NonNull
    Long id;

    @NonNull
    Long userId;

    @JsonProperty("created_at")
    Instant createdAt;

    String orderStatus;

    BigDecimal orderPrice;

    List<OrderItemDto> orderItems = new ArrayList<>();
}
