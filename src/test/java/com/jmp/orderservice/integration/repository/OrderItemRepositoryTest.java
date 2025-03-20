package com.jmp.orderservice.integration.repository;

import com.jmp.orderservice.model.Order;
import com.jmp.orderservice.model.OrderItem;
import com.jmp.orderservice.model.StatusOrder;
import com.jmp.orderservice.model.StatusPayment;
import com.jmp.orderservice.repository.OrderItemRepository;
import com.jmp.orderservice.repository.OrderRepository;
import com.jmp.orderservice.support.BaseIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderItemRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Transactional
    void testOrderSave() {
        Order order = getOrder();
        orderRepository.save(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(12345L);
        orderItem.setQuantity(2);
        orderItem.setPrice(new BigDecimal("19.99"));
        orderItem.setCreatedAt(LocalDateTime.now());
        orderItem.setUpdatedAt(LocalDateTime.now());
        orderItem.setOrder(order);

        orderItemRepository.save(orderItem);

        OrderItem orderById = orderItemRepository.findById(orderItem.getId()).orElse(null);
        assertThat(orderById).isNotNull();
        assertThat(orderById)
                .usingRecursiveComparison()
                .isEqualTo(orderItem);
    }

    private Order getOrder() {
        Order order = new Order();
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(StatusOrder.PAID);
        order.setPaymentStatus(StatusPayment.CANCELLED);
        order.setDeliveryDate(LocalDateTime.now().plusDays(5));
        order.setUserId(UUID.randomUUID());
        return order;
    }
}