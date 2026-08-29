package com.ana.olist.dashboards.sale.service

import com.ana.olist.dashboards.sale.dto.AverageOrderValueDTO
import com.ana.olist.dashboards.sale.dto.HighestRevenueByProductCategoriesDTO
import com.ana.olist.dashboards.sale.dto.MonthlyRevenueDTO
import com.ana.olist.dashboards.sale.dto.MonthlyRevenueSummaryDTO
import com.ana.olist.dashboards.sale.dto.Top10BestSellingProductsDTO
import com.ana.olist.dashboards.sale.repository.RevenueAnalyticsRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service


@Service
@RequiredArgsConstructor
class RevenueAnalyticsService(
    private val revenueAnalyticsRepository: RevenueAnalyticsRepository
) {

    fun getMonthlyRevenue(): List<MonthlyRevenueDTO> {

        val monthlyRevenue: MutableList<MonthlyRevenueDTO> = mutableListOf()
        revenueAnalyticsRepository.findMonthlyRevenueDashboard()
            .forEach {
                monthlyRevenue.add(
                    MonthlyRevenueDTO(
                        year = it.getYear(),
                        month = it.getMonth(),
                        currentTotalRevenue = it.getCurrentTotalRevenue(),
                        prevMonthRevenue = it.getPrevMonthRevenue(),
                        momGrowth = it.getMomGrowth(),
                    )
                )
            }

        return monthlyRevenue
    }


    fun getMonthlyRevenueSummary(): List<MonthlyRevenueSummaryDTO> {

        return revenueAnalyticsRepository.findMonthlyRevenueSummaryDashboard()
            .map {
                MonthlyRevenueSummaryDTO(
                    year = it.getYear(),
                    month = it.getMonth(),
                    totalRevenue = it.getTotalRevenue()
                )
            }
    }

    fun getHighestRevenue(): List<HighestRevenueByProductCategoriesDTO> {

        return revenueAnalyticsRepository.findHighestRevenueByProductCategoriesDashboard()
            .map {
                HighestRevenueByProductCategoriesDTO(
                    productCategoryNameEnglish = it.getProductCategoryNameEnglish(),
                    totalRevenue = it.getTotalRevenue(),
                    rankTotalRevenue = it.getRankTotalRevenue()
                )
            }
    }
    fun getTop10BestSellingProducts(): List<Top10BestSellingProductsDTO> {

        return revenueAnalyticsRepository.findTop10BestSellingProductsDashboard()
            .map {
                Top10BestSellingProductsDTO(
                    productId =  it.getProductId(),
                    productCategoryNameEnglish = it.getProductCategoryNameEnglish(),
                    countOrder = it.getCountOrder(),
                    rankCountOrderId = it.getRankCountOrderId()
                )
            }
    }
    fun getAverageOrderValue(): List<AverageOrderValueDTO> {
        return revenueAnalyticsRepository.findAverageOrderValueDashboard()
            .map {
                AverageOrderValueDTO(
                    totalRevenue = it.getTotalRevenue(),
                    totalOrders = it.getTotalOrders(),
                    aov = it.getAov()
                )
            }
    }
}