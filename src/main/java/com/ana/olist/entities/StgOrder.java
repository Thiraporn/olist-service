package com.ana.olist.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "stg_orders")
public class StgOrder {

    @Id
    @Column(name = "order_id", length = 50)
    private String orderId;

    @Column(name = "customer_id", length = 50)
    private String customerId;

    @Column(name = "order_status", length = 50)
    private String orderStatus;

    @Column(name = "order_purchase_timestamp")
    private LocalDateTime orderPurchaseTimestamp;

    @Column(name = "order_approved_at")
    private LocalDateTime orderApprovedAt;

    @Column(name = "order_delivered_carrier_date")
    private LocalDateTime orderDeliveredCarrierDate;

    @Column(name = "order_delivered_customer_date")
    private LocalDateTime orderDeliveredCustomerDate;

    @Column(name = "order_estimated_delivery_date")
    private LocalDateTime orderEstimatedDeliveryDate;

}