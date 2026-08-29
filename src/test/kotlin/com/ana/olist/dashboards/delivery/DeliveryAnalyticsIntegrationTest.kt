package com.ana.olist.dashboards.delivery

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("test")
class DeliveryAnalyticsIntegrationTest {
    @Value("\${authen.service.url}")
    lateinit var authenServiceUrl: String


    @Value("\${olist.service.url}")
    lateinit var olistServiceUrl: String


    @Autowired
    lateinit var objectMapper: ObjectMapper


    private val restTemplate = RestTemplate()

    @Test
    fun `should return average delivery with jwt`() {
        // Login Auth Service

        val loginBody = mapOf(
            "user" to "springbootit",
            "pwd" to "012347890"
        )


        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }


        val loginResponse = restTemplate.postForEntity(
            "$authenServiceUrl/authen",
            HttpEntity(loginBody, headers),
            String::class.java
        )


        assertEquals(
            HttpStatus.OK,
            loginResponse.statusCode
        )
        // ---------- Parse Access Token ----------
        val token =  objectMapper.readTree(loginResponse.body) ["accessToken"].asText()

        // Call Ecommerce API


        val apiHeaders = HttpHeaders().apply {
            setBearerAuth(token)
        }

        //http://localhost:9091/api/delivery/average_delivery
        val response = restTemplate.exchange(
            "$olistServiceUrl/api/delivery/average_delivery",
            HttpMethod.GET,
            HttpEntity<Void>(apiHeaders),
            String::class.java
        )


        assertEquals(
            HttpStatus.OK,
            response.statusCode
        )
    }
}