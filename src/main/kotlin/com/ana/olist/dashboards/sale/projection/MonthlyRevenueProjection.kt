package com.ana.olist.dashboards.sale.projection

import java.math.BigDecimal

interface MonthlyRevenueProjection {
    fun getYear(): Int

    fun getMonth(): Int

    fun getCurrentTotalRevenue(): BigDecimal

    fun getPrevMonthRevenue(): BigDecimal

    fun getMomGrowth(): BigDecimal
}

