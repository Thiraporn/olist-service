package com.ana.olist.dashboards.delivery.projection

import java.math.BigDecimal

interface TotalFreightPerOrderProjection {
    fun getAvgFreight(): BigDecimal
}