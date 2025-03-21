package com.jmp.orderservice.integration.repository;

import com.jmp.orderservice.model.OrderItem;
import com.jmp.orderservice.repository.OrderItemRepository;
import com.jmp.orderservice.support.BaseIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderItemRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    @Transactional
    void shouldSaveOrderItemInDatabaseWhenValid() {
        OrderItem orderItem = new OrderItem();
        orderItemRepository.save(orderItem);
        orderItem.setProductId(12345L);
        orderItem.setQuantity(2);
        orderItem.setPrice(new BigDecimal("19.99"));
        orderItem.setCreatedAt(LocalDateTime.now());
        orderItem.setUpdatedAt(LocalDateTime.now());

        orderItemRepository.save(orderItem);

        OrderItem orderById = orderItemRepository.findById(orderItem.getId()).orElse(null);
        assertThat(orderById).isNotNull();
        assertThat(orderById)
                .usingRecursiveComparison()
                .isEqualTo(orderItem);
    }

    @Test
    @Transactional
    void shouldUpdateOrderItemInDatabaseWhenValid() {
        OrderItem orderItem = getOrderItem();
        orderItem.setQuantity(3);
        orderItem.setPrice(new BigDecimal("15.99"));
        orderItemRepository.save(orderItem);

        OrderItem orderById = orderItemRepository.findById(orderItem.getId()).orElse(null);
        assertThat(orderById).isNotNull();
        assertThat(orderById)
                .usingRecursiveComparison()
                .isEqualTo(orderItem);

    }

    private OrderItem getOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(12345L);
        orderItem.setQuantity(2);
        orderItem.setPrice(new BigDecimal("19.99"));
        orderItem.setCreatedAt(LocalDateTime.now());
        orderItem.setUpdatedAt(LocalDateTime.now());
        return orderItem;
    }
}