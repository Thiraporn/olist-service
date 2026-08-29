package com.ana.olist.dashboards.delivery.projection

import java.math.BigDecimal

interface AverageDeliveryProjection {
    fun getAvgDaysOfDelivery(): BigDecimal
}