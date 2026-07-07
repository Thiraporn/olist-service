package com.ana.olist.dashboards.sale.DTOs

import java.math.BigDecimal

data class MonthlyRevenueDTO(
    val year: Int,//Kotlin แนะนำใช้ Int แทน Integer เพราะ Kotlin มี Primitive Type ของตัวเอง และออกแบบมาให้ใช้งานง่ายกว่า Java
    val month: Int,
    val currentTotalRevenue: BigDecimal,
    val prevMonthRevenue: BigDecimal,
    val momGrowth: BigDecimal
)