package com.ana.olist.dashboards.customer.projection

interface HighestRankingCustomerCityProjection {
    fun getCustomerCity(): String
    fun getCountPurchasedCustomers(): Int
    fun getCountRegisteredCustomers(): Int
    fun getRank(): Int
}