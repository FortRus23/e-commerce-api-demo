package ru.sakhapov.e_commerce_api.store.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sakhapov.e_commerce_api.api.dto.product.ProductCreateDto;
import ru.sakhapov.e_commerce_api.api.dto.product.ProductDto;
import ru.sakhapov.e_commerce_api.api.exception.NotFoundException;
import ru.sakhapov.e_commerce_api.api.factory.ProductDtoFactory;
import ru.sakhapov.e_commerce_api.store.entity.Brand;
import ru.sakhapov.e_commerce_api.store.entity.Category;
import ru.sakhapov.e_commerce_api.store.entity.Product;
import ru.sakhapov.e_commerce_api.store.repository.BrandRepository;
import ru.sakhapov.e_commerce_api.store.repository.CategoryRepository;
import ru.sakhapov.e_commerce_api.store.repository.ProductRepository;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    ProductDtoFactory productDtoFactory;
    BrandRepository brandRepository;

    @Transactional(readOnly = true)
    public Page<ProductDto> findAllFiltered(String prefixName, String categoryName, String brandName, Pageable pageable) {
        Page<Product> page;

        boolean hasPrefix = prefixName != null && !prefixName.trim().isEmpty();
        boolean hasCategory = categoryName != null && !categoryName.trim().isEmpty();
        boolean hasBrand = brandName != null && !brandName.trim().isEmpty();

        if (hasPrefix && hasCategory && hasBrand) {
            page = productRepository.findByNameStartingWithIgnoreCaseAndCategoryNameIgnoreCaseAndBrandNameIgnoreCase(
                    prefixName.trim(), categoryName.trim(), brandName.trim(), pageable);
        } else if (hasPrefix && hasCategory) {
            page = productRepository.findByNameStartingWithIgnoreCaseAndCategoryNameIgnoreCase(
                    prefixName.trim(), categoryName.trim(), pageable);
        } else if (hasPrefix && hasBrand) {
            page = productRepository.findByNameStartingWithIgnoreCaseAndBrandNameIgnoreCase(
                    prefixName.trim(), brandName.trim(), pageable);
        } else if (hasCategory && hasBrand) {
            page = productRepository.findByCategoryNameIgnoreCaseAndBrandNameIgnoreCase(
                    categoryName.trim(), brandName.trim(), pageable);
        } else if (hasPrefix) {
            page = productRepository.findByNameStartingWithIgnoreCase(prefixName.trim(), pageable);
        } else if (hasCategory) {
            page = productRepository.findByCategoryNameIgnoreCase(categoryName.trim(), pageable);
        } else if (hasBrand) {
            page = productRepository.findByBrandNameIgnoreCase(brandName.trim(), pageable);
        } else {
            page = productRepository.findAll(pageable);
        }

        return page.map(productDtoFactory::makeProductDto);
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->  new NotFoundException("Product not found with id = " + id));
    }

    @Transactional
    public Product save(ProductCreateDto productDto) {

        Category category = null;
        if (productDto.getCategoryId() != null) {
            category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() ->
                            new NotFoundException(String.format("Category with id = '%s' not found", productDto.getCategoryId())));
        }

        Brand brand = null;
        if (productDto.getBrandId() != null) {
            brand = brandRepository.findById(productDto.getBrandId())
                    .orElseThrow(() ->
                            new NotFoundException(String.format("Brand with id = '%s' not found", productDto.getBrandId())));
        }

        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .isAvailable(productDto.isAvailable())
                .category(category)
                .brand(brand)
                .build();

        return productRepository.save(product);
    }

    @Transactional
    public Product update(Long id, ProductCreateDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id = " + id));

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setAvailable(productDto.isAvailable());

        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() ->
                            new NotFoundException(String.format("Category with id = '%s' not found", productDto.getCategoryId())));

            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        if (productDto.getBrandId() != null) {
            Brand brand = brandRepository.findById(productDto.getBrandId())
                    .orElseThrow(() ->
                            new NotFoundException(String.format("Brand with id = '%s' not found", productDto.getBrandId())));

            product.setBrand(brand);
        } else {
            product.setBrand(null);
        }

        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

}
