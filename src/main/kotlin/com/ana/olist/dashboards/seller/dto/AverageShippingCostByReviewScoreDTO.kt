package com.ana.olist.dashboards.seller.dto

import java.math.BigDecimal

data class AverageShippingCostByReviewScoreDTO(
    val reviewScore: Int,
    val totalOrders:Int,
    val avgShippingCost: BigDecimal
)
