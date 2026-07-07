package com.ana.olist.dashboards.sale.services

import com.ana.olist.dashboards.sale.DTOs.MonthlyRevenueDTO
import com.ana.olist.dashboards.sale.DTOs.MonthlyRevenueSummaryDTO
import com.ana.olist.dashboards.sale.repository.RevenueRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service


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
}