package com.ana.olist.dashboards

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dashboard")
class DashboardController(
    private val dashboardService: DashboardService
) {

    @GetMapping("/hello")
    fun hello(): String {
        return dashboardService.hello()
    }

}