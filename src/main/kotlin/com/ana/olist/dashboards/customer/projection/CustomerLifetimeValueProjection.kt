package com.ana.olist.dashboards.customer.projection

import java.math.BigDecimal

interface CustomerLifetimeValueProjection {
    fun getAvgSpending(): BigDecimal
}