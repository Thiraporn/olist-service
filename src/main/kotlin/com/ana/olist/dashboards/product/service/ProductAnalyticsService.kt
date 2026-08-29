package com.ana.olist.dashboards.product.service

import com.ana.olist.dashboards.delivery.dto.AverageDeliveryDTO
import com.ana.olist.dashboards.product.dto.AveragePriceOfProductDTO
import com.ana.olist.dashboards.product.repository.ProductAnalyticsRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.math.BigDecimal
import kotlin.String

@Service
class ProductAnalyticsService(
    private val productAnalyticsRepository: ProductAnalyticsRepository

) {
    fun getAveragePriceOfProduct(): List<AveragePriceOfProductDTO> {
        return productAnalyticsRepository.findAveragePriceOfProductDashboard()
            .map {
                AveragePriceOfProductDTO(
                     productCategoryNameEnglish= it.getProductCategoryNameEnglish(),
                     avgPrice= it.getAvgPrice(),
                     totalItemsSold= it.getTotalItemsSold()
                )
            }
    }

}