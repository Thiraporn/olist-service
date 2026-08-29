package com.ana.olist.dashboards.sale.projection

import java.math.BigDecimal

interface HighestRevenueByProductCategoriesProjection {
    fun getProductCategoryNameEnglish(): String
    fun getTotalRevenue(): BigDecimal
    fun getRankTotalRevenue(): Int
}