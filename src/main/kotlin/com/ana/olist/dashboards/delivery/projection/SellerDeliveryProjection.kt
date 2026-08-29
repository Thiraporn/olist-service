package com.ana.olist.dashboards.delivery.projection

import java.math.BigDecimal

interface SellerDeliveryProjection {
    fun getSellerId(): String
    fun getTotalOrders(): Int
    fun getAvgSellerDelivered(): BigDecimal
}