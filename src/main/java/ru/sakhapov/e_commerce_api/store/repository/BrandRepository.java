package ru.sakhapov.e_commerce_api.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sakhapov.e_commerce_api.store.entity.Brand;


@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Page<Brand> findByNameStartingWithIgnoreCase(String prefix, Pageable pageable);
    Page<Brand> findAll(Pageable pageable);
}
