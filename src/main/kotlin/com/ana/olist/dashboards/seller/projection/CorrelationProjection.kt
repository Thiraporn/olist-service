package com.ana.olist.dashboards.seller.projection

import java.math.BigDecimal

interface CorrelationProjection {
    fun getCorrelation(): BigDecimal
}