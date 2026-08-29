package com.ana.olist.dashboards.product

import com.ana.olist.dashboards.product.projection.AveragePriceOfProductProjection
import com.ana.olist.dashboards.product.repository.ProductAnalyticsRepository
import com.ana.olist.dashboards.product.service.ProductAnalyticsService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import kotlin.collections.get


@ExtendWith(MockitoExtension::class)
class ProductAnalyticsServiceTest {

    @Mock
    lateinit var repository: ProductAnalyticsRepository

    @InjectMocks
    lateinit var service: ProductAnalyticsService
//    @BeforeEach
//    fun setup() {
//        service = ProductAnalyticsService(repository)
//    }
    @Test
    fun `should return average price of product`() {

        val projection = mock<AveragePriceOfProductProjection>()

        whenever(projection.getProductCategoryNameEnglish())
            .thenReturn("bed_bath_table")

        whenever(projection.getAvgPrice())
            .thenReturn(BigDecimal("189.90"))

        whenever(projection.getTotalItemsSold())
            .thenReturn(2500 )

        whenever(repository.findAveragePriceOfProductDashboard())
            .thenReturn(listOf(projection))

        val result = service.getAveragePriceOfProduct()

        assertEquals(1, result.size)
        assertEquals("bed_bath_table", result[0].productCategoryNameEnglish)
        assertEquals(BigDecimal("189.90"), result[0].avgPrice)
        assertEquals(2500, result[0].totalItemsSold)

        verify(repository).findAveragePriceOfProductDashboard()
    }
}