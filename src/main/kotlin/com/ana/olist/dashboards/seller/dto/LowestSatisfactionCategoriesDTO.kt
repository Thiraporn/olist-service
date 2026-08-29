package com.ana.olist.dashboards.seller.dto

import java.math.BigDecimal

data class LowestSatisfactionCategoriesDTO(
    val productCategoryNameEnglish: String,
    val countReviews: Int,
    val reviewScoreByProductCate: BigDecimal,
)
