package com.billing.service

import com.billing.dto.DoctorRegistrationRequest
import com.billing.dto.DoctorResponse
import com.billing.mapper.EntityMapper.toDomain
import com.billing.mapper.EntityMapper.toEntity
import com.billing.repository.DoctorRepository
import jakarta.inject.Singleton

@Singleton
open class DoctorService(
    private val doctorRepository: DoctorRepository
) {

    fun registerDoctor(request: DoctorRegistrationRequest): DoctorResponse {
        val doctor = request.toDoctor()
        val doctorEntity = doctor.toEntity()
        val savedEntity = doctorRepository.save(doctorEntity)
        val savedDoctor = savedEntity.toDomain()
        return DoctorResponse.fromDoctor(savedDoctor)
    }

    fun getAllDoctors(): List<DoctorResponse> {
        return doctorRepository.findAll().map { entity ->
            DoctorResponse.fromDoctor(entity.toDomain())
        }
    }
}
