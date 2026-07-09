package com.ana.olist.dashboards.product.dto

import java.math.BigDecimal

data class AveragePriceOfProductDTO(
    val productCategoryNameEnglish: String,
    val avgPrice: BigDecimal,
    val totalItemsSold: Int
)
