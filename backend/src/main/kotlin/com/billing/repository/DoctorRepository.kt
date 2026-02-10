package com.billing.repository

import com.billing.model.Doctor
import jakarta.inject.Singleton
import java.util.concurrent.ConcurrentHashMap

@Singleton
open class DoctorRepository {

    private val doctors = ConcurrentHashMap<String, Doctor>()

    fun save(doctor: Doctor): Doctor {
        doctors[doctor.npiNumber] = doctor
        return doctor
    }

    fun findAll(): List<Doctor> {
        return doctors.values.toList()
    }
}
