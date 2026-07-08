package com.ana.olist.dashboards.sale.dto

import java.math.BigDecimal

data class MonthlyRevenueSummaryDTO(
    val year: Int,
    val month: Int,
    val totalRevenue: BigDecimal
)