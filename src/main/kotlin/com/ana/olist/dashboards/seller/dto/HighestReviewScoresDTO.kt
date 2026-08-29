package com.ana.olist.dashboards.seller.dto

import java.math.BigDecimal

data class HighestReviewScoresDTO(
    val sellerId: String,
    val countReviews: Int,
    val reviewScoreBySeller: BigDecimal,
)
