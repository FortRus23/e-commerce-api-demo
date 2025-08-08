package ru.sakhapov.e_commerce_api.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sakhapov.e_commerce_api.api.dto.AckDto;
import ru.sakhapov.e_commerce_api.api.dto.product.ProductCreateDto;
import ru.sakhapov.e_commerce_api.api.dto.product.ProductDto;
import ru.sakhapov.e_commerce_api.api.factory.ProductDtoFactory;
import ru.sakhapov.e_commerce_api.store.entity.Product;
import ru.sakhapov.e_commerce_api.store.service.ProductService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/products")
public class ProductController {

    ProductDtoFactory productDtoFactory;
    ProductService productService;

    @GetMapping
    public Page<ProductDto> getProducts(
            @RequestParam(value = "prefix_name", required = false) String prefixName,
            @RequestParam(value = "category", required = false) String categoryName,
            @RequestParam(value = "brand", required = false) String brandName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return productService.findAllFiltered(prefixName, categoryName, brandName, pageable);
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return productDtoFactory.makeProductDto(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductDto createProduct(@RequestBody ProductCreateDto dto) {
        Product saved = productService.save(dto);
        return productDtoFactory.makeProductDto(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductCreateDto productDto) {
        Product updated = productService.update(id, productDto);
        return productDtoFactory.makeProductDto(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public AckDto deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return AckDto.makeDefault(true);
    }
}
