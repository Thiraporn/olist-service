package com.ana.olist.dashboards.sale.controllers

import com.ana.olist.dashboards.sale.DTOs.MonthlyRevenueDTO
import com.ana.olist.dashboards.sale.DTOs.MonthlyRevenueSummaryDTO
import com.ana.olist.dashboards.sale.services.RevenueService
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

}