package com.ana.olist.dashboards.customer.controller

import com.ana.olist.dashboards.customer.dto.CustomerLifetimeValueDTO
import com.ana.olist.dashboards.customer.dto.CustomersContributeDTO
import com.ana.olist.dashboards.customer.dto.HighestRankingCustomerCityDTO
import com.ana.olist.dashboards.customer.dto.MadePurchasesCustomersDTO
import com.ana.olist.dashboards.customer.dto.RepeatCustomerDTO
import com.ana.olist.dashboards.customer.service.CustomerAnalyticsService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
class CustomerAnalyticsController(private val customerAnalyticsService: CustomerAnalyticsService) {
    @GetMapping("/made_purchases_customers")
    fun getMadePurchasesCustomers(): List<MadePurchasesCustomersDTO> {
        return customerAnalyticsService.getMadePurchasesCustomers();
    }

    @GetMapping("/repeat_customer")
    fun getRepeatCustomer(): List<RepeatCustomerDTO> {
        return customerAnalyticsService.getRepeatCustomer();
    }

    @GetMapping("/CLV")
    fun getCustomerLifetimeValue(): List<CustomerLifetimeValueDTO> {
        return customerAnalyticsService.getCustomerLifetimeValue();
    }

    @GetMapping("/highest_ranking_customer_city")
    fun getHighestRankingCustomerCity(): List<HighestRankingCustomerCityDTO> {
        return customerAnalyticsService.getHighestRankingCustomerCity();
    }

    @GetMapping("/customers_contribute")
    fun getCustomersContribute(): List<CustomersContributeDTO> {
        return customerAnalyticsService.getCustomersContribute();
    }
}