package com.ana.olist.dashboards.seller.projection

import java.math.BigDecimal

interface AverageShippingCostByReviewScoreProjection {
    fun getReviewScore(): Int
    fun getTotalOrders():Int
    fun getAvgShippingCost(): BigDecimal
}