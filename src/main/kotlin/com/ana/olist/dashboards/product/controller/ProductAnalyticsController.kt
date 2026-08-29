package com.ana.olist.dashboards.product.controller

import com.ana.olist.dashboards.product.dto.AveragePriceOfProductDTO
import com.ana.olist.dashboards.product.service.ProductAnalyticsService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
class ProductAnalyticsController(private val productAnalyticsService: ProductAnalyticsService) {
    @GetMapping("/average_delivery")
    fun getAveragePriceOfProduct(): List<AveragePriceOfProductDTO> {
        return productAnalyticsService.getAveragePriceOfProduct();
    }

}