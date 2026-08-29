package com.ana.olist.dashboards.delivery.dto

import java.math.BigDecimal

data class EstimatedDeliveryPercentageDTO(
    val  countAllDelivery: Int,
    val  countDelay: Int,
    val  percentOfDelay: BigDecimal,
    val  percentOfOntime: BigDecimal
)
