package ru.sakhapov.e_commerce_api.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sakhapov.e_commerce_api.store.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameStartingWithIgnoreCase(String name, Pageable pageable);
    Page<Product> findByCategoryNameIgnoreCase(String categoryName, Pageable pageable);
    Page<Product> findByBrandNameIgnoreCase(String brandName, Pageable pageable);

    Page<Product> findByNameStartingWithIgnoreCaseAndCategoryNameIgnoreCase(String name, String category, Pageable pageable);
    Page<Product> findByNameStartingWithIgnoreCaseAndBrandNameIgnoreCase(String name, String brand, Pageable pageable);
    Page<Product> findByCategoryNameIgnoreCaseAndBrandNameIgnoreCase(String category, String brand, Pageable pageable);

    Page<Product> findByNameStartingWithIgnoreCaseAndCategoryNameIgnoreCaseAndBrandNameIgnoreCase(String name, String category, String brand, Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.category = NULL WHERE p.category.id = :categoryId")
    void clearCategoryFromProducts(Long categoryId);

    @Modifying
    @Query("UPDATE Product p SET p.brand = NULL WHERE p.brand.id = :brandId")
    void clearBrandFromProducts(Long brandId);
}
