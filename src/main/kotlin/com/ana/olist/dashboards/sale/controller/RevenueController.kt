package com.ana.olist.dashboards.sale.controller

import com.ana.olist.dashboards.sale.dto.AverageOrderValueDTO
import com.ana.olist.dashboards.sale.dto.HighestRevenueByProductCategoriesDTO
import com.ana.olist.dashboards.sale.dto.MonthlyRevenueDTO
import com.ana.olist.dashboards.sale.dto.MonthlyRevenueSummaryDTO
import com.ana.olist.dashboards.sale.dto.Top10BestSellingProductsDTO
import com.ana.olist.dashboards.sale.service.RevenueService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/revenue")
@RequiredArgsConstructor
class RevenueController(
    private val revenueService: RevenueService
) {
    @GetMapping("/monthly")
    fun getMonthlyRevenue(): List<MonthlyRevenueDTO> {
        return revenueService.getMonthlyRevenue()

    }

    @GetMapping("/monthly-summary")
    fun getMonthlyRevenueSummary(): List<MonthlyRevenueSummaryDTO> {
        return revenueService.getMonthlyRevenueSummary()
    }

    @GetMapping("/highest-revenue-category")
    fun getHighestRevenue(): List<HighestRevenueByProductCategoriesDTO> {

        return revenueService.getHighestRevenue()
    }

    @GetMapping("/Top10BestSellingProducts")
    fun getTop10BestSellingProducts(): List<Top10BestSellingProductsDTO> {
        return revenueService.getTop10BestSellingProducts()
    }

    @GetMapping("/aov")
    fun getAverageOrderValue(): List<AverageOrderValueDTO> {
        return revenueService.getAverageOrderValue()
    }

}