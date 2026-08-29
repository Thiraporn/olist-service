package com.ana.olist.dashboards.delivery.projection

import java.math.BigDecimal

interface EstimatedDeliveryPercentageProjection {
    fun  getCountAllDelivery(): Int
    fun  getCountDelay(): Int
    fun  getPercentOfDelay(): BigDecimal
    fun  getPercentOfOntime(): BigDecimal
}