package com.ana.olist.dashboards.product.projection

import java.math.BigDecimal

interface AveragePriceOfProductProjection {
    fun getProductCategoryNameEnglish():  String
    fun getAvgPrice():  BigDecimal
    fun getTotalItemsSold():  Int
}