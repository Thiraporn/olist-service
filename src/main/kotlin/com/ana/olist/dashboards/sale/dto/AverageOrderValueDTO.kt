package com.ana.olist.dashboards.sale.dto

import java.math.BigDecimal

data class AverageOrderValueDTO(
   val totalRevenue: BigDecimal,
   val totalOrders:Int,
   val aov: BigDecimal
)
