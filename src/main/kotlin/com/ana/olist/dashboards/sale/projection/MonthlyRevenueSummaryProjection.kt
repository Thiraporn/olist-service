package com.ana.olist.dashboards.sale.projection

import java.math.BigDecimal

interface  MonthlyRevenueSummaryProjection {
    fun getYear(): Int

    fun getMonth(): Int

    fun getTotalRevenue(): BigDecimal

}

