package com.ana.olist.dashboards.seller.dto

import java.math.BigDecimal

data class SellersRevenueDTO(
    val sellerId: String,
    val sellersRevenue: BigDecimal,
)
