package com.billing.controller

import com.billing.dto.PatientRegistrationRequest
import com.billing.dto.PatientResponse
import com.billing.service.PatientService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import jakarta.validation.Valid

@Controller("/api/patients")
open class PatientController(
    private val patientService: PatientService
) {

    @Post
    open fun registerPatient(@Body @Valid request: PatientRegistrationRequest): HttpResponse<PatientResponse> {
        val response = patientService.registerPatient(request)
        return HttpResponse.created(response)
    }

}
