package ru.sakhapov.e_commerce_api.store.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sakhapov.e_commerce_api.api.dto.order.OrderDto;
import ru.sakhapov.e_commerce_api.api.dto.order.OrderItemDto;
import ru.sakhapov.e_commerce_api.api.exception.NotFoundException;
import ru.sakhapov.e_commerce_api.api.exception.OrderAccessDeniedException;
import ru.sakhapov.e_commerce_api.api.factory.OrderDtoFactory;
import ru.sakhapov.e_commerce_api.store.entity.*;
import ru.sakhapov.e_commerce_api.store.repository.CartItemRepository;
import ru.sakhapov.e_commerce_api.store.repository.CartRepository;
import ru.sakhapov.e_commerce_api.store.repository.OrderItemRepository;
import ru.sakhapov.e_commerce_api.store.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {

    CurrentUserService currentUserService;
    CartRepository cartRepository;
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    CartItemRepository cartItemRepository;
    OrderDtoFactory orderDtoFactory;

    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersForCurrentUser() {
        User user = currentUserService.getCurrentUser();

        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);

        return orders.stream().map(order -> {
            OrderDto dto = new OrderDto();
            dto.setId(order.getId());
            dto.setUserId(user.getId());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setOrderStatus(order.getOrderStatus().name());
            dto.setOrderPrice(order.getOrderPrice());

            List<OrderItemDto> itemsDto = order.getOrderItems().stream().map(item -> {
                OrderItemDto itemDto = new OrderItemDto();
                itemDto.setId(item.getId());
                itemDto.setProductId(item.getProduct().getId());
                itemDto.setProductName(item.getProduct().getName());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setPrice(item.getPrice());
                return itemDto;
            }).toList();

            dto.setOrderItems(itemsDto);

            return dto;
        }).toList();
    }

    @Transactional
    public OrderDto createOrderFromCart() {
        User user = currentUserService.getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Cart is empty"));

        List<CartItem> cartItems = cart.getItems();
        if(cartItems.isEmpty()) {
            throw new NotFoundException("Корзина пуста, замменить в OrderService");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(Instant.now());
        order.setOrderStatus(OrderStatus.PROCESSING);

        var totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            totalPrice = totalPrice.add(orderItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItems.add(orderItem);
        }

        order.setOrderPrice(totalPrice);

        //order
        Order saveOrder = orderRepository.save(order);
        orderItems.forEach(i -> i.setOrder(saveOrder));
        orderItemRepository.saveAll(orderItems);

        //cart
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);

        //dto // TODO: // dto factory
        OrderDto dto = new OrderDto();
        dto.setId(saveOrder.getId());
        dto.setUserId(user.getId());
        dto.setCreatedAt(saveOrder.getCreatedAt());
        dto.setOrderStatus(saveOrder.getOrderStatus().name());
        dto.setOrderPrice(saveOrder.getOrderPrice());

        List<OrderItemDto> orderItemDtos = orderItems.stream().map(item -> {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setId(item.getId());
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            return itemDto;
        }).toList();

        dto.setOrderItems(orderItemDtos);

        return dto;
    }

    @Transactional
    public OrderDto payOrder(Long orderId) {
        User user = currentUserService.getCurrentUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderAccessDeniedException();
        }

        if (order.getOrderStatus() != OrderStatus.PROCESSING) {
            throw new IllegalStateException("Order is not in PROCESSING state");
        }

        order.setOrderStatus(OrderStatus.PAID);
        Order saved = orderRepository.save(order);

        return orderDtoFactory.makeOrderDto(saved);
    }
}
