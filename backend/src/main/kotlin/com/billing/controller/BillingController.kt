package com.billing.controller

import com.billing.dto.BillingResponse
import com.billing.service.BillingService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import jakarta.validation.constraints.NotBlank

@Controller("/api/billing")
open class BillingController(
    private val billingService: BillingService
) {

    @Get("/generate")
    open fun generateBill(
        @QueryValue @NotBlank patientId: String,
        @QueryValue @NotBlank doctorNpiNumber: String
    ): HttpResponse<BillingResponse> {

        val response = billingService.generateBill(
            patientId = patientId,
            doctorNpiNumber = doctorNpiNumber
        )
        return HttpResponse.ok(response)
    }
}
