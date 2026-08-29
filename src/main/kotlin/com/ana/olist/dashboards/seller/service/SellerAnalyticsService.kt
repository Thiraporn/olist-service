package com.ana.olist.dashboards.seller.service

import com.ana.olist.dashboards.product.dto.AveragePriceOfProductDTO
import com.ana.olist.dashboards.seller.dto.AverageShippingCostByReviewScoreDTO
import com.ana.olist.dashboards.seller.dto.CorrelationDTO
import com.ana.olist.dashboards.seller.dto.HighestReviewScoresDTO
import com.ana.olist.dashboards.seller.dto.LowestSatisfactionCategoriesDTO
import com.ana.olist.dashboards.seller.dto.SellersRevenueDTO
import com.ana.olist.dashboards.seller.repository.SellerAnalyticsRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.math.BigDecimal
import kotlin.String

@Service
@RequiredArgsConstructor
class SellerAnalyticsService(private val sellerAnalyticsRepository: SellerAnalyticsRepository) {
    fun getSellersRevenue(): List<SellersRevenueDTO> {
        return sellerAnalyticsRepository.findSellersRevenueDashboard()
            .map {
                SellersRevenueDTO(
                    sellerId = it.getSellerId(),
                    sellersRevenue = it.getSellersRevenue()
                )
            }
    }

    fun getHighestReviewScores(): List<HighestReviewScoresDTO> {
        return sellerAnalyticsRepository.findHighestReviewScoresDashboard()
            .map {
                HighestReviewScoresDTO(
                    sellerId = it.getSellerId(),
                    countReviews = it.getCountReviews(),
                    reviewScoreBySeller = it.getReviewScoreBySeller()
                )
            }
    }

    fun getLowestSatisfactionCategories(): List<LowestSatisfactionCategoriesDTO> {
        return sellerAnalyticsRepository.findLowestSatisfactionCategoriesDashboard()
            .map {
                LowestSatisfactionCategoriesDTO(
                    productCategoryNameEnglish = it.getProductCategoryNameEnglish(),
                    countReviews = it.getCountReviews(),
                    reviewScoreByProductCate = it.getReviewScoreByProductCate()
                )
            }
    }

    fun getCorrelation(): List<CorrelationDTO> {
        return sellerAnalyticsRepository.findCorrelationDashboard()
            .map {
                CorrelationDTO(
                    correlation = it.getCorrelation(),
                )
            }
    }

    fun getAverageShippingCostByReviewScore(): List<AverageShippingCostByReviewScoreDTO> {
        return sellerAnalyticsRepository.findAverageShippingCostByReviewScoreDashboard()
            .map {
                AverageShippingCostByReviewScoreDTO(
                    reviewScore = it.getReviewScore(),
                    totalOrders = it.getTotalOrders(),
                    avgShippingCost = it.getAvgShippingCost(),
                )
            }
    }


}