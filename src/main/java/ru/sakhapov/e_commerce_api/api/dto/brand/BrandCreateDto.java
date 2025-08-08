package ru.sakhapov.e_commerce_api.api.dto.brand;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandCreateDto {

    @NonNull
    String name;
}
