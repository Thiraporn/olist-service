package com.ana.olist.dashboards.delivery.dto

import java.math.BigDecimal

data class SellerDeliveryDTO(
    val sellerId: String,
    val totalOrders: Int,
    val avgSellerDelivered: BigDecimal
)
