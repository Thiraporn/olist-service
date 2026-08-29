package com.ana.olist.dashboards.seller.controller

import com.ana.olist.dashboards.seller.dto.AverageShippingCostByReviewScoreDTO
import com.ana.olist.dashboards.seller.dto.CorrelationDTO
import com.ana.olist.dashboards.seller.dto.HighestReviewScoresDTO
import com.ana.olist.dashboards.seller.dto.LowestSatisfactionCategoriesDTO
import com.ana.olist.dashboards.seller.dto.SellersRevenueDTO
import com.ana.olist.dashboards.seller.service.SellerAnalyticsService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
class SellerAnalyticsController(private val sellerAnalyticsService: SellerAnalyticsService) {

    @GetMapping("/sellers_revenue")
    fun getSellersRevenue(): List<SellersRevenueDTO> {
        return sellerAnalyticsService.getSellersRevenue();
    }


    @GetMapping("/highest_review_scores")
    fun getHighestReviewScores(): List<HighestReviewScoresDTO> {
        return sellerAnalyticsService.getHighestReviewScores();
    }


    @GetMapping("/lowest_satisfaction_categories")
    fun getLowestSatisfactionCategories(): List<LowestSatisfactionCategoriesDTO> {
        return sellerAnalyticsService.getLowestSatisfactionCategories();
    }

    @GetMapping("/correlation")
    fun getCorrelation(): List<CorrelationDTO> {
        return sellerAnalyticsService.getCorrelation();
    }

    @GetMapping("/average_shipping_cost_by_review_score")
    fun getCorrelationAverageShippingCostByReviewScore(): List<AverageShippingCostByReviewScoreDTO> {
        return sellerAnalyticsService.getAverageShippingCostByReviewScore();
    }

}