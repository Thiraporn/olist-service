package com.ana.olist.dashboards.seller.projection

import java.math.BigDecimal

interface LowestSatisfactionCategoriesProjection {
    fun getProductCategoryNameEnglish(): String
    fun getCountReviews(): Int
    fun getReviewScoreByProductCate(): BigDecimal
}