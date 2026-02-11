package com.billing.service

import com.billing.dto.PatientRegistrationRequest
import com.billing.dto.PatientResponse
import com.billing.mapper.EntityMapper.toDomain
import com.billing.mapper.EntityMapper.toEntity
import com.billing.repository.PatientRepository
import jakarta.inject.Singleton

@Singleton
open class PatientService(
    private val patientRepository: PatientRepository
) {

    fun registerPatient(request: PatientRegistrationRequest): PatientResponse {
        val patient = request.toPatient("")
        val patientEntity = patient.toEntity()
        val savedEntity = patientRepository.save(patientEntity)
        val savedPatient = savedEntity.toDomain()
        return PatientResponse.fromPatient(savedPatient)
    }

    fun getAllPatients(): List<PatientResponse> {
        return patientRepository.findAll().map { entity ->
            PatientResponse.fromPatient(entity.toDomain())
        }
    }

}
