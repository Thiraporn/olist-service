package com.ana.olist.dashboards.sale.dto

data class Top10BestSellingProductsDTO(
   val productId: String,
   val productCategoryNameEnglish: String,
   val countOrder: Int,
   val rankCountOrderId:Int
)
