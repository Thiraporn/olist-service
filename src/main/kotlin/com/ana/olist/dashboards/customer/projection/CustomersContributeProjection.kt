package com.ana.olist.dashboards.customer.projection

import java.math.BigDecimal

interface CustomersContributeProjection {
    fun getCalRevenueShare(): BigDecimal
}