package ru.sakhapov.e_commerce_api.store.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sakhapov.e_commerce_api.api.dto.category.CategoryCreateDto;
import ru.sakhapov.e_commerce_api.api.dto.category.CategoryDto;
import ru.sakhapov.e_commerce_api.api.exception.NotFoundException;
import ru.sakhapov.e_commerce_api.api.factory.CategoryDtoFactory;
import ru.sakhapov.e_commerce_api.store.entity.Category;
import ru.sakhapov.e_commerce_api.store.repository.CategoryRepository;
import ru.sakhapov.e_commerce_api.store.repository.ProductRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryDtoFactory categoryDtoFactory;
    ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDto> findAll(Optional<String> prefixName, Pageable pageable) {
        Page<Category> categoryPage = prefixName
                .filter(s -> !s.trim().isEmpty())
                .map(s -> categoryRepository.findByNameStartingWithIgnoreCase(s.trim(), pageable))
                .orElseGet(() -> categoryRepository.findAll(pageable));

        return categoryPage.map(categoryDtoFactory::makeCategoryDto);
    }

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id = '%s' not found", id)));
    }

    @Transactional
    public Category save(CategoryCreateDto createDto) {
        Category category = Category.builder()
                .name(createDto.getName())
                .build();

        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long id, CategoryCreateDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->  new NotFoundException(String.format("Category with id = '%s' not found", id)));

        category.setName(dto.getName());

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->  new NotFoundException(String.format("Category with id = '%s' not found", id)));

        productRepository.clearCategoryFromProducts(id);
        categoryRepository.delete(category);
    }
}
