package com.ana.olist.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "fact_orders")
public class FactOrder {

    @Id
    @Column(name = "order_id", length = 50)
    private String orderId;

    @Column(name = "customer_id", length = 50)
    private String customerId;

    @Column(name = "order_date_id")
    private Integer orderDateId;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "delivery_fee", precision = 18, scale = 2)
    private BigDecimal deliveryFee;

    @Column(name = "total_revenue_by_orderitems", precision = 18, scale = 2)
    private BigDecimal totalRevenueByOrderitems;

    @Column(name = "total_revenue_by_payment", precision = 18, scale = 2)
    private BigDecimal totalRevenueByPayment;

    @Column(name = "delivery_time_days")
    private Integer deliveryTimeDays;

    @Column(name = "estimated_delivery_days")
    private Integer estimatedDeliveryDays;

    @Column(name = "delay_days")
    private Integer delayDays;

    @Column(name = "on_time")
    private Integer onTime;

    @Column(name = "review_score")
    private Double reviewScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}