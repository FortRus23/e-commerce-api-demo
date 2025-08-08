package ru.sakhapov.e_commerce_api.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sakhapov.e_commerce_api.api.dto.AckDto;
import ru.sakhapov.e_commerce_api.api.dto.brand.BrandCreateDto;
import ru.sakhapov.e_commerce_api.api.dto.brand.BrandDto;
import ru.sakhapov.e_commerce_api.api.factory.BrandDtoFactory;
import ru.sakhapov.e_commerce_api.store.entity.Brand;
import ru.sakhapov.e_commerce_api.store.service.BrandService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/brands")
public class BrandController {

    BrandService brandService;
    BrandDtoFactory brandDtoFactory;

    @GetMapping
    public Page<BrandDto> getBrands(
            @RequestParam(value = "prefix_name", required = false) String prefixName,
            Pageable pageable
    ) {
        Optional<String> optionalPrefixName = Optional.ofNullable(prefixName);
        return brandService.findAll(optionalPrefixName, pageable);
    }

    @GetMapping("/{id}")
    public BrandDto getCategoryById(@PathVariable Long id) {
        Brand brand = brandService.findById(id);
        return brandDtoFactory.makeBrandDto(brand);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public BrandDto createCategory(@RequestBody BrandCreateDto dto) {
        Brand brand = brandService.save(dto);
        return brandDtoFactory.makeBrandDto(brand);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public BrandDto updateCategory(@PathVariable Long id, @RequestBody BrandCreateDto brandDto) {
        Brand brand = brandService.update(id, brandDto);
        return brandDtoFactory.makeBrandDto(brand);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public AckDto deleteBrand(@PathVariable Long id) {
        brandService.deleteById(id);
        return AckDto.makeDefault(true);
    }
}
