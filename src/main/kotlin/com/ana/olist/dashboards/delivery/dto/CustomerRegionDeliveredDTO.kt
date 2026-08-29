package com.ana.olist.dashboards.delivery.dto

import java.math.BigDecimal

data class CustomerRegionDeliveredDTO(
    val customerState: String,
    val totalOrders: Int,
    val avgCustomerDelivered: BigDecimal
)
