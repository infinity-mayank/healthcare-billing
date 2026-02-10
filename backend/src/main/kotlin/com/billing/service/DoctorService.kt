package com.billing.service

import com.billing.dto.DoctorRegistrationRequest
import com.billing.dto.DoctorResponse
import com.billing.repository.DoctorRepository
import jakarta.inject.Singleton

@Singleton
open class DoctorService(
    private val doctorRepository: DoctorRepository
) {

    fun registerDoctor(request: DoctorRegistrationRequest): DoctorResponse {
        val doctor = request.toDoctor()
        val savedDoctor = doctorRepository.save(doctor)
        return DoctorResponse.fromDoctor(savedDoctor)
    }
}
