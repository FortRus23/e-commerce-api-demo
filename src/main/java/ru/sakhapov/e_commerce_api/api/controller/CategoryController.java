package ru.sakhapov.e_commerce_api.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sakhapov.e_commerce_api.api.dto.AckDto;
import ru.sakhapov.e_commerce_api.api.dto.category.CategoryCreateDto;
import ru.sakhapov.e_commerce_api.api.dto.category.CategoryDto;
import ru.sakhapov.e_commerce_api.api.factory.CategoryDtoFactory;
import ru.sakhapov.e_commerce_api.store.entity.Category;
import ru.sakhapov.e_commerce_api.store.service.CategoryService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/categories")
public class CategoryController {

    CategoryService categoryService;
    CategoryDtoFactory categoryDtoFactory;

    @GetMapping
    public Page<CategoryDto> getCategories(
            @RequestParam(value = "prefix_name", required = false) String prefixName,
            Pageable pageable
    ) {
        Optional<String> optionalPrefixName = Optional.ofNullable(prefixName);
        return categoryService.findAll(optionalPrefixName, pageable);
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return categoryDtoFactory.makeCategoryDto(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryCreateDto dto) {
        Category category = categoryService.save(dto);
        return categoryDtoFactory.makeCategoryDto(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryCreateDto categoryDto) {
        Category category = categoryService.update(id, categoryDto);
        return categoryDtoFactory.makeCategoryDto(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public AckDto deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return AckDto.makeDefault(true);
    }
}
