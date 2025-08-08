package ru.sakhapov.e_commerce_api.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sakhapov.e_commerce_api.store.entity.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByNameStartingWithIgnoreCase(String prefix, Pageable pageable);
    Page<Category> findAll(Pageable pageable);
}
