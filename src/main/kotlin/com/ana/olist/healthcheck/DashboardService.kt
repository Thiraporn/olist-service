package com.ana.olist.healthcheck

import org.springframework.stereotype.Service

@Service
class DashboardService {

    fun hello(): String {
        return "Hello Dashboard Service"
    }

}