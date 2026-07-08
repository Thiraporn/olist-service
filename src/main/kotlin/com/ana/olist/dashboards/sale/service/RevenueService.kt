package com.ana.olist.dashboards.sale.service

import com.ana.olist.dashboards.sale.dto.AverageOrderValueDTO
import com.ana.olist.dashboards.sale.dto.HighestRevenueByProductCategoriesDTO
import com.ana.olist.dashboards.sale.dto.MonthlyRevenueDTO
import com.ana.olist.dashboards.sale.dto.MonthlyRevenueSummaryDTO
import com.ana.olist.dashboards.sale.dto.Top10BestSellingProductsDTO
import com.ana.olist.dashboards.sale.repository.RevenueRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.math.BigDecimal
import kotlin.String


@Service
@RequiredArgsConstructor
class RevenueService(
    private val revenueRepository: RevenueRepository
) {

    fun getMonthlyRevenue(): List<MonthlyRevenueDTO> {

        val monthlyRevenue: MutableList<MonthlyRevenueDTO> = mutableListOf()
        revenueRepository.findMonthlyRevenueReport()
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

        return revenueRepository.findMonthlyRevenueSummary()
            .map {
                MonthlyRevenueSummaryDTO(
                    year = it.getYear(),
                    month = it.getMonth(),
                    totalRevenue = it.getTotalRevenue()
                )
            }
    }

    fun getHighestRevenue(): List<HighestRevenueByProductCategoriesDTO> {

        return revenueRepository.findHighestRevenueByProductCategories()
            .map {
                HighestRevenueByProductCategoriesDTO(
                    productCategoryNameEnglish = it.getProductCategoryNameEnglish(),
                    totalRevenue = it.getTotalRevenue(),
                    rankTotalRevenue = it.getRankTotalRevenue()
                )
            }
    }
    fun getTop10BestSellingProducts(): List<Top10BestSellingProductsDTO> {

        return revenueRepository.findTop10BestSellingProducts()
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
        return revenueRepository.findAverageOrderValue()
            .map {
                AverageOrderValueDTO(
                    totalRevenue = it.getTotalRevenue(),
                    totalOrders = it.getTotalOrders(),
                    aov = it.getAov()
                )
            }
    }
}