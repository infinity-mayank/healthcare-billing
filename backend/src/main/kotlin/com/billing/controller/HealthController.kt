package com.billing.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/api/health")
open class HealthController {
    @Get
    fun healthCheck(): String {
        return "System is running and healthy"
    }
}