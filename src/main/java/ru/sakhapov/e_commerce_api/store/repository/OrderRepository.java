package ru.sakhapov.e_commerce_api.store.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sakhapov.e_commerce_api.store.entity.Order;
import ru.sakhapov.e_commerce_api.store.entity.User;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"orderItems", "orderItems.product"})
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
