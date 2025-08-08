package ru.sakhapov.e_commerce_api.api.factory;

import org.springframework.stereotype.Component;
import ru.sakhapov.e_commerce_api.api.dto.brand.BrandDto;
import ru.sakhapov.e_commerce_api.store.entity.Brand;

@Component
public class BrandDtoFactory {

    public BrandDto makeBrandDto(Brand brand) {
        return BrandDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .productsCount(brand.getProducts().size())
                .build();
    }

}
