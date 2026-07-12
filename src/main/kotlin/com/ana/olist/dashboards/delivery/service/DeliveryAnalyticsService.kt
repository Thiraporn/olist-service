package com.ana.olist.dashboards.delivery.service

import com.ana.olist.dashboards.delivery.dto.AverageDeliveryDTO
import com.ana.olist.dashboards.delivery.dto.CustomerRegionDeliveredDTO
import com.ana.olist.dashboards.delivery.dto.EstimatedDeliveryPercentageDTO
import com.ana.olist.dashboards.delivery.dto.SellerDeliveryDTO
import com.ana.olist.dashboards.delivery.dto.TotalFreightPerOrderDTO
import com.ana.olist.dashboards.delivery.repository.DeliveryAnalyticsRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service



@Service
@RequiredArgsConstructor
class DeliveryAnalyticsService(private val deliveryAnalyticsRepository: DeliveryAnalyticsRepository) {
    fun getAverageDelivery(): List<AverageDeliveryDTO> {
        return deliveryAnalyticsRepository.findAverageDeliveryDashboard()
            .map {
                AverageDeliveryDTO(
                    avgDaysOfDelivery = it.getAvgDaysOfDelivery()
                )
            }
    }

    fun getEstimatedDeliveryPercentage(): List<EstimatedDeliveryPercentageDTO> {
        return deliveryAnalyticsRepository.findEstimatedDeliveryPercentageDashboard()
            .map {
                EstimatedDeliveryPercentageDTO(
                    countAllDelivery = it.getCountAllDelivery(),
                    countDelay = it.getCountAllDelivery(),
                    percentOfDelay = it.getPercentOfDelay(),
                    percentOfOntime = it.getPercentOfOntime()
                )
            }
    }

    fun getSellerDelivery(): List<SellerDeliveryDTO> {
        return deliveryAnalyticsRepository.findSellerDeliveryDashboard()
            .map {
                SellerDeliveryDTO(
                    sellerId = it.getSellerId(),
                    totalOrders = it.getTotalOrders(),
                    avgSellerDelivered = it.getAvgSellerDelivered()
                )
            }
    }

    fun getCustomerRegionDelivered(): List<CustomerRegionDeliveredDTO> {
        return deliveryAnalyticsRepository.findCustomerRegionDeliveredDashboard()
            .map {
                CustomerRegionDeliveredDTO(
                    customerState = it.getCustomerState(),
                    totalOrders = it.getTotalOrders(),
                    avgCustomerDelivered = it.getAvgCustomerDelivered()
                )
            }
    }

    fun getTotalFreightPerOrder(): List<TotalFreightPerOrderDTO> {
        return deliveryAnalyticsRepository.findTotalFreightPerOrderDashboard()
            .map {
                TotalFreightPerOrderDTO(
                    avgFreight = it.getAvgFreight(),
                )
            }
    }
}