package com.ana.olist.dashboards.seller.projection

import java.math.BigDecimal

interface SellersRevenueProjection {
    fun getSellerId(): String
    fun getSellersRevenue(): BigDecimal
}