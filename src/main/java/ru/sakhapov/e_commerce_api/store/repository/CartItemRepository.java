package ru.sakhapov.e_commerce_api.store.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sakhapov.e_commerce_api.store.entity.Cart;
import ru.sakhapov.e_commerce_api.store.entity.CartItem;
import ru.sakhapov.e_commerce_api.store.entity.Product;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @EntityGraph(attributePaths = {"product"})
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}
