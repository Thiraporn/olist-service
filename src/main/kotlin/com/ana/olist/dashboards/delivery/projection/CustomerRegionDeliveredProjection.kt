package com.ana.olist.dashboards.delivery.projection

import java.math.BigDecimal

interface CustomerRegionDeliveredProjection {
    fun getCustomerState(): String
    fun getTotalOrders(): Int
    fun getAvgCustomerDelivered(): BigDecimal
}