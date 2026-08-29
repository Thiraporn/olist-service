package com.ana.olist.dashboards.customer

import com.ana.olist.dashboards.customer.projection.CustomerLifetimeValueProjection
import com.ana.olist.dashboards.customer.projection.CustomersContributeProjection
import com.ana.olist.dashboards.customer.projection.HighestRankingCustomerCityProjection
import com.ana.olist.dashboards.customer.projection.MadePurchasesCustomersProjection
import com.ana.olist.dashboards.customer.projection.RepeatCustomerProjection
import com.ana.olist.dashboards.customer.repository.CustomerAnalyticsRepository
import com.ana.olist.dashboards.customer.service.CustomerAnalyticsService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import java.math.BigDecimal

class CustomerAnalyticsServiceTest {
    @Mock
    lateinit var repository: CustomerAnalyticsRepository

    lateinit var service: CustomerAnalyticsService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        service = CustomerAnalyticsService(repository)
    }

    @Test
    fun `should return made purchases customers`() {
        val projection = object : MadePurchasesCustomersProjection {
            override fun getMadePurchasesCustomers() = 96
            override fun getAllCustomers() = 100
        }

        whenever(repository.findMadePurchasesCustomersDashboard())
            .thenReturn(listOf(projection))

        val result = service.getMadePurchasesCustomers()

        assertEquals(1, result.size)
        assertEquals(96, result[0].madePurchasesCustomers)
        assertEquals(100, result[0].allCustomers)
    }

    @Test
    fun `should return repeat customer`() {
        val projection = object : RepeatCustomerProjection {
            override fun getRepeatCustomer() = 50
            override fun getMadePurchasesCustomers() = 100
            override fun getRepeatCustomerRate() = 50
        }

        whenever(repository.findRepeatCustomerDashboard())
            .thenReturn(
                listOf(projection)
            )

        val result = service.getRepeatCustomer()

        assertEquals(1, result.size)
        assertEquals(50, result[0].repeatCustomer)
        assertEquals(100, result[0].madePurchasesCustomers)
        assertEquals(50, result[0].repeatCustomerRate)
    }

    @Test
    fun `should return customer lifetime value`() {
        val projection = object : CustomerLifetimeValueProjection {
            override fun getAvgSpending() = BigDecimal("487.50")
        }

        whenever(repository.findCustomerLifetimeValueDashboard())
            .thenReturn(
                listOf(projection)
            )

        val result = service.getCustomerLifetimeValue()

        assertEquals(1, result.size)
        assertEquals(
            BigDecimal("487.50"),
            result[0].avgSpending
        )
    }


    @Test
    fun `should return highest ranking customer city`() {

        val projection = object : HighestRankingCustomerCityProjection {

            override fun getCustomerCity() = "sao paulo"

            override fun getCountPurchasedCustomers() = 100

            override fun getCountRegisteredCustomers() = 150

            override fun getRank() = 1
        }

        whenever(repository.findHighestRankingCustomerCityDashboard())
            .thenReturn(listOf(projection))

        val result = service.getHighestRankingCustomerCity()

        assertEquals(1, result.size)
        assertEquals("sao paulo", result[0].customerCity)
        assertEquals(100, result[0].countPurchasedCustomers)
        assertEquals(150, result[0].countRegisteredCustomers)
        assertEquals(1, result[0].rank)
    }

    @Test
    fun `should return customers contribute`() {

        val projection = object : CustomersContributeProjection {
            override fun getCalRevenueShare() =
                BigDecimal("80.50")
        }

        whenever(repository.findCustomersContributeDashboard())
            .thenReturn(listOf(projection))

        val result = service.getCustomersContribute()

        assertEquals(1, result.size)
        assertEquals(
            BigDecimal("80.50"),
            result[0].calRevenueShare
        )
    }

}