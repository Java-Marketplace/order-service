package com.jmp.orderservice.integration.repository;

import com.jmp.orderservice.model.Order;
import com.jmp.orderservice.model.StatusOrder;
import com.jmp.orderservice.model.StatusPayment;
import com.jmp.orderservice.repository.OrderRepository;
import com.jmp.orderservice.support.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Transactional
    void shouldSaveOrderInDatabaseWhenValid() {
        Order order = new Order();
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(StatusOrder.PAID);
        order.setPaymentStatus(StatusPayment.CANCELLED);
        order.setDeliveryDate(LocalDateTime.now().plusDays(5));
        order.setUserId(UUID.randomUUID());

        orderRepository.save(order);

        Order orderById = orderRepository.findById(order.getId()).orElse(null);
        assertThat(orderById).isNotNull();
        assertThat(orderById)
                .usingRecursiveComparison()
                .isEqualTo(order);
    }

    @Test
    @Transactional
    void shouldUpdateOrderWhenValid() {
        Order order = getOrder();
        orderRepository.save(order);
        Optional<Order> orderUpdateOpt = orderRepository.findById(order.getId());
        assertTrue(orderUpdateOpt.isPresent());
        Order orderUpdate = orderUpdateOpt.get();
        orderUpdate.setStatus(StatusOrder.CANCELLED);
        orderUpdate.setPaymentStatus(StatusPayment.PAID);
        orderRepository.save(orderUpdate);

        Order orderById = orderRepository.findById(orderUpdate.getId()).orElse(null);
        assertThat(orderById).isNotNull();
        assertThat(orderById)
                .usingRecursiveComparison()
                .isEqualTo(orderUpdate);
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
