package ru.sakhapov.e_commerce_api.api.factory;

import org.springframework.stereotype.Component;
import ru.sakhapov.e_commerce_api.api.dto.category.CategoryDto;
import ru.sakhapov.e_commerce_api.store.entity.Category;

@Component
public class CategoryDtoFactory {

    public CategoryDto makeCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .productsCount(category.getProducts().size())
                .build();
    }
}
