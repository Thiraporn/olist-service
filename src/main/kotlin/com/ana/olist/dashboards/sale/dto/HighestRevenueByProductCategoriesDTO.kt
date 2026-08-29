package com.ana.olist.dashboards.sale.dto

import java.math.BigDecimal

data class HighestRevenueByProductCategoriesDTO(
    val productCategoryNameEnglish: String,
    val totalRevenue: BigDecimal,
    val rankTotalRevenue: Int
)