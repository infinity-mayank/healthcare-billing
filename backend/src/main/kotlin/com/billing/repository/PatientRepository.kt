package com.billing.repository

import com.billing.model.Patient
import jakarta.inject.Singleton
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Singleton
open class PatientRepository {

    private val patients = ConcurrentHashMap<String, Patient>()
    private val idCounter = AtomicInteger(0)

    fun save(patient: Patient): Patient {
        val savedPatient = if (patient.id.isEmpty()) {
            patient.copy(id = "P${idCounter.incrementAndGet().toString().padStart(4, '0')}")
        } else {
            patient
        }
        patients[savedPatient.id] = savedPatient
        return savedPatient
    }

    fun findAll(): List<Patient> {
        return patients.values.toList()
    }

}
