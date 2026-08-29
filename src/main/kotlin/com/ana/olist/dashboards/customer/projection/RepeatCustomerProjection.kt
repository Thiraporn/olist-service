package com.ana.olist.dashboards.customer.projection

interface RepeatCustomerProjection {
    fun getRepeatCustomer(): Int
    fun getMadePurchasesCustomers(): Int
    fun getRepeatCustomerRate(): Int
}