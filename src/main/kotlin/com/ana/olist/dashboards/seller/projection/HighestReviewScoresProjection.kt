package com.ana.olist.dashboards.seller.projection

import java.math.BigDecimal

interface HighestReviewScoresProjection {
    fun getSellerId(): String
    fun getCountReviews(): Int
    fun getReviewScoreBySeller(): BigDecimal
}