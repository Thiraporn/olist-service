package com.ana.olist.dashboards.product


import com.ana.olist.dashboards.product.dto.AveragePriceOfProductDTO
import com.ana.olist.dashboards.product.service.ProductAnalyticsService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import kotlin.test.assertEquals


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductAnalyticsIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var service: ProductAnalyticsService

    @Autowired
    lateinit var objectMapper: ObjectMapper
    lateinit var accessToken: String

    @Value("\${server.url:http://localhost}")
    lateinit var serverUrl: String

    @Value("\${server.port:8080}")
    lateinit var serverPort: String


    @Test
    fun testUrl() {
        val baseUrl = "$serverUrl:$serverPort"
        println(baseUrl)
    }

    @BeforeEach
    fun login() {
        data class LoginRequest(
            val user: String,
            val pwd: String
        )

        // ---------- Login ----------
        val loginRequest = LoginRequest(
            user = "springbootit",
            pwd = "012347890"
        )
        val restTemplate = RestTemplate()
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity(
            loginRequest,
            headers
        )
        val baseUrl = "$serverUrl:$serverPort"
        val loginResponse = restTemplate.postForEntity(
            //"$baseUrl/authen",
            "http://localhost:9090/authen",
            request,
            String::class.java
        )

        assertEquals(
            HttpStatus.OK,
            loginResponse.statusCode
        )

        // ---------- Parse Access Token ----------
        val jsonNode = objectMapper.readTree(loginResponse.body)
        accessToken = jsonNode["accessToken"].asText()
    }

    @Test
    fun `should return average price of product`() {

        whenever(service.getAveragePriceOfProduct())
            .thenReturn(
                listOf(
                    AveragePriceOfProductDTO(
                        productCategoryNameEnglish = "bed_bath_table",
                        avgPrice = BigDecimal("189.90"),
                        totalItemsSold = 10
                    )
                )
            )

        mockMvc.get("/api/product/average_delivery") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
                jsonPath("$[0].productCategoryNameEnglish").value("bed_bath_table")
                jsonPath("$[0].avgPrice").value(189.90)
                jsonPath("$[0].totalItemsSold").value(10)
            }
    }



}