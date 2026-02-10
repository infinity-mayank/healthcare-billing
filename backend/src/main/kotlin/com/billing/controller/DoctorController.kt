package com.billing.controller

import com.billing.dto.DoctorRegistrationRequest
import com.billing.dto.DoctorResponse
import com.billing.service.DoctorService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import jakarta.validation.Valid

@Controller("/api/doctors")
open class DoctorController(
    private val doctorService: DoctorService
) {

    @Post
    open fun registerDoctor(@Body @Valid request: DoctorRegistrationRequest): HttpResponse<DoctorResponse> {
        val response = doctorService.registerDoctor(request)
        return HttpResponse.created(response)
    }
}