package ru.sakhapov.e_commerce_api.store.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sakhapov.e_commerce_api.api.dto.brand.BrandCreateDto;
import ru.sakhapov.e_commerce_api.api.dto.brand.BrandDto;
import ru.sakhapov.e_commerce_api.api.exception.NotFoundException;
import ru.sakhapov.e_commerce_api.api.factory.BrandDtoFactory;
import ru.sakhapov.e_commerce_api.store.entity.Brand;
import ru.sakhapov.e_commerce_api.store.entity.Category;
import ru.sakhapov.e_commerce_api.store.repository.BrandRepository;
import ru.sakhapov.e_commerce_api.store.repository.ProductRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandService {

    BrandRepository brandRepository;
    BrandDtoFactory brandDtoFactory;
    ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<BrandDto> findAll(Optional<String> prefixName, Pageable pageable) {
        Page<Brand> brandsPage = prefixName
                .filter(s -> !s.trim().isEmpty())
                .map(s -> brandRepository.findByNameStartingWithIgnoreCase(s.trim(), pageable))
                .orElseGet(() -> brandRepository.findAll(pageable));

        return brandsPage.map(brandDtoFactory::makeBrandDto);
    }

    @Transactional(readOnly = true)
    public Brand findById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Brand with id = '%s' not found", id)));
    }

    @Transactional
    public Brand save(BrandCreateDto createDto) {
        Brand brand = Brand.builder()
                .name(createDto.getName())
                .build();

        return brandRepository.save(brand);
    }

    @Transactional
    public Brand update(Long id, BrandCreateDto dto) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Brand with id = '%s' not found", id)));

        brand.setName(dto.getName());

        return brandRepository.save(brand);
    }

    @Transactional
    public void deleteById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() ->  new NotFoundException(String.format("Brand with id = '%s' not found", id)));

        productRepository.clearBrandFromProducts(id);
        brandRepository.delete(brand);
    }

}
