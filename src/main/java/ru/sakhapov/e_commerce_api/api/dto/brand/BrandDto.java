package ru.sakhapov.e_commerce_api.api.dto.brand;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class BrandDto {

    @NonNull
    Long id;

    @NonNull
    String name;

    Integer productsCount;

}
