package ru.sakhapov.e_commerce_api.api.factory;

import org.springframework.stereotype.Component;
import ru.sakhapov.e_commerce_api.api.dto.product.ProductDto;
import ru.sakhapov.e_commerce_api.store.entity.Product;

@Component
public class ProductDtoFactory {

    public ProductDto makeProductDto(Product product) {

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .isAvailable(product.isAvailable())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .build();
    }
}
