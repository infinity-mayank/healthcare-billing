package com.billing.controller

import com.billing.dto.BillResponse
import com.billing.dto.SaveBillRequest
import com.billing.dto.SaveBillResponse
import com.billing.service.BillService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

@Controller("/api/bill")
open class BillController(
    private val billService: BillService
) {

    @Get("/generate")
    open fun generateBill(
        @QueryValue @NotBlank patientId: String,
        @QueryValue @NotBlank doctorNpiNumber: String
    ): HttpResponse<BillResponse> {

        val response = billService.generateBill(
            patientId = patientId,
            doctorNpiNumber = doctorNpiNumber
        )
        return HttpResponse.ok(response)
    }

    @Post("/save")
    open fun saveBill(@Body @Valid request: SaveBillRequest): HttpResponse<SaveBillResponse> {
        val response = billService.saveBill(request)
        return HttpResponse.created(response)
    }
}
