package com.ana.olist.dashboards.delivery.controller

import com.ana.olist.dashboards.delivery.dto.AverageDeliveryDTO
import com.ana.olist.dashboards.delivery.dto.CustomerRegionDeliveredDTO
import com.ana.olist.dashboards.delivery.dto.EstimatedDeliveryPercentageDTO
import com.ana.olist.dashboards.delivery.dto.SellerDeliveryDTO
import com.ana.olist.dashboards.delivery.dto.TotalFreightPerOrderDTO
import com.ana.olist.dashboards.delivery.service.DeliveryAnalyticsService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
class DeliveryAnalyticsController(private val deliveryAnalyticsService: DeliveryAnalyticsService) {
    @GetMapping("/average_delivery")
    fun getAverageDelivery(): List<AverageDeliveryDTO> {
        return deliveryAnalyticsService.getAverageDelivery();
    }

    @GetMapping("/estimated_delivery_percentage")
    fun getEstimatedDeliveryPercentage(): List<EstimatedDeliveryPercentageDTO> {
        return deliveryAnalyticsService.getEstimatedDeliveryPercentage();
    }

    @GetMapping("/seller_delivery")
    fun getSellerDelivery(): List<SellerDeliveryDTO> {
        return deliveryAnalyticsService.getSellerDelivery();
    }

    @GetMapping("/customer_region_delivered")
    fun getCustomerRegionDelivered(): List<CustomerRegionDeliveredDTO> {
        return deliveryAnalyticsService.getCustomerRegionDelivered();
    }

    @GetMapping("/total_freight_perOrder")
    fun getTotalFreightPerOrder(): List<TotalFreightPerOrderDTO> {
        return deliveryAnalyticsService.getTotalFreightPerOrder();
    }
}