package com.ana.olist.dashboards.sale.projection

interface Top10BestSellingProductsProjection {
    fun getProductId(): String
    fun getProductCategoryNameEnglish(): String
    fun getCountOrder(): Int
    fun getRankCountOrderId():Int
}