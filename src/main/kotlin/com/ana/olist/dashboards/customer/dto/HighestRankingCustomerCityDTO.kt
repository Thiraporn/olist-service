package com.ana.olist.dashboards.customer.dto

data class HighestRankingCustomerCityDTO(
    val customerCity: String,
    val countPurchasedCustomers:Int,
    val countRegisteredCustomers:Int,
    val rank:Int
)
