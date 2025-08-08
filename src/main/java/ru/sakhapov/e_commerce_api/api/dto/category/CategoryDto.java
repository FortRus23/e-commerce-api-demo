package ru.sakhapov.e_commerce_api.api.dto.category;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class CategoryDto {

    @NonNull
    Long id;

    @NonNull
    String name;

    Integer productsCount;

}
