package com.ana.olist.dashboards.sale.projection

import java.math.BigDecimal

interface AverageOrderValueProjection {
    fun getTotalRevenue(): BigDecimal
    fun getTotalOrders():Int
    fun getAov(): BigDecimal
}