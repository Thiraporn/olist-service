package com.ana.olist.dashboards.sale.DTOs

import java.math.BigDecimal

data class MonthlyRevenueSummaryDTO(
    val year: Int,
    val month: Int,
    val totalRevenue: BigDecimal
)