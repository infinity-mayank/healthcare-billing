package com.billing.service

import com.billing.dto.PatientRegistrationRequest
import com.billing.dto.PatientResponse
import com.billing.repository.PatientRepository
import jakarta.inject.Singleton

@Singleton
open class PatientService(
    private val patientRepository: PatientRepository
) {

    fun registerPatient(request: PatientRegistrationRequest): PatientResponse {
        val patient = request.toPatient("")
        val savedPatient = patientRepository.save(patient)
        return PatientResponse.fromPatient(savedPatient)
    }

}
