package com.ana.olist.dashboards.customer.service

import com.ana.olist.dashboards.customer.dto.CustomerLifetimeValueDTO
import com.ana.olist.dashboards.customer.dto.CustomersContributeDTO
import com.ana.olist.dashboards.customer.dto.HighestRankingCustomerCityDTO
import com.ana.olist.dashboards.customer.dto.MadePurchasesCustomersDTO
import com.ana.olist.dashboards.customer.dto.RepeatCustomerDTO
import com.ana.olist.dashboards.customer.repository.CustomerAnalyticsRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service


@Service
@RequiredArgsConstructor
class CustomerAnalyticsService(
    private val customerAnalyticsRepository: CustomerAnalyticsRepository
) {
    fun getMadePurchasesCustomers(): List<MadePurchasesCustomersDTO> {
        return customerAnalyticsRepository.findMadePurchasesCustomersDashboard()
            .map {
                MadePurchasesCustomersDTO(
                    madePurchasesCustomers = it.getMadePurchasesCustomers(),
                    allCustomers = it.getAllCustomers(),
                )
            }
    }

    fun getRepeatCustomer(): List<RepeatCustomerDTO> {
        return customerAnalyticsRepository.findRepeatCustomerDashboard()
            .map {
                RepeatCustomerDTO(
                    repeatCustomer = it.getRepeatCustomer(),
                    madePurchasesCustomers = it.getMadePurchasesCustomers(),
                    repeatCustomerRate = it.getRepeatCustomerRate(),
                )
            }
    }

    fun getCustomerLifetimeValue(): List<CustomerLifetimeValueDTO> {
        return customerAnalyticsRepository.findCustomerLifetimeValueDashboard()
            .map {
                CustomerLifetimeValueDTO(
                    avgSpending = it.getAvgSpending(),
                )
            }
    }

    fun getHighestRankingCustomerCity(): List<HighestRankingCustomerCityDTO> {
        return customerAnalyticsRepository.findHighestRankingCustomerCityDashboard()
            .map {
                HighestRankingCustomerCityDTO(
                    customerCity = it.getCustomerCity(),
                    countPurchasedCustomers = it.getCountPurchasedCustomers(),
                    countRegisteredCustomers = it.getCountRegisteredCustomers(),
                    rank = it.getRank(),
                )
            }
    }

    fun getCustomersContribute(): List<CustomersContributeDTO> {
        return customerAnalyticsRepository.findCustomersContributeDashboard()
            .map {
                CustomersContributeDTO(
                    calRevenueShare = it.getCalRevenueShare(),
                )
            }
    }


}