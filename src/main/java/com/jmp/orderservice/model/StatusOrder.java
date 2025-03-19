package com.jmp.orderservice.model;

public enum StatusOrder {
    NEW,
    PENDING_PAYMENT,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUNDED
}
