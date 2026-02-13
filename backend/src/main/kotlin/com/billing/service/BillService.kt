package com.billing.service

import com.billing.config.BillingConfiguration
import com.billing.dto.BillResponse
import com.billing.dto.SaveBillRequest
import com.billing.dto.SaveBillResponse
import com.billing.entity.BillEntity
import com.billing.entity.PatientEntity
import com.billing.mapper.EntityMapper.toDomain
import com.billing.model.Doctor
import com.billing.repository.BillRepository
import com.billing.repository.DoctorRepository
import com.billing.repository.PatientRepository
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Singleton
import java.util.UUID

@Singleton
open class BillService(
    private val doctorRepository: DoctorRepository,
    private val patientRepository: PatientRepository,
    private val billingConfiguration: BillingConfiguration,
    private val billRepository: BillRepository
) {

    fun generateBill(patientId: String, doctorNpiNumber: String): BillResponse {
        val billEntity = calculateBill(patientId, doctorNpiNumber)

        return BillResponse(
            patientId = billEntity.patientId.toString(),
            doctorNpiNumber = billEntity.doctorNpiNumber,
            consultationFee = billEntity.consultationFee,
            taxAmount = billEntity.taxAmount,
            totalAmount = billEntity.totalAmount,
            coPayAmount = billEntity.coPayAmount,
            insurancePayableAmount = billEntity.insurancePayableAmount,
            taxRatePercentage = billEntity.taxRatePercentage,
            coPayRatePercentage = billEntity.coPayRatePercentage,
            discountAmount = billEntity.discountAmount,
            discountPercentage = billEntity.discountPercentage
        )
    }

    fun saveBill(saveBillRequest: SaveBillRequest): SaveBillResponse {
        val billEntity = calculateBill(saveBillRequest.patientId, saveBillRequest.doctorNpiNumber)

        val savedBill = billRepository.save(billEntity)

        return SaveBillResponse(
            id = savedBill.id.toString(),
            createdAt = savedBill.createdAt.toString()
        )
    }

    private fun calculateBill(
        patientId: String,
        doctorNpiNumber: String
    ): BillEntity {
        val patientUuid = parsePatientId(patientId)

        getPatient(patientUuid)
        val doctor = getDoctor(doctorNpiNumber)

        val consultationFee = calculateConsultationFee(doctor)

        val discountPercentage = calculateDiscountPercentage(patientUuid)
        val discountAmount = calculateDiscountAmount(consultationFee, discountPercentage)

        val discountedFee = consultationFee - discountAmount
        val taxAmount = calculateTax(discountedFee)
        val totalAmount = discountedFee + taxAmount

        val coPayAmount = calculateCoPay(totalAmount)
        val insurancePayableAmount = totalAmount - coPayAmount

        return BillEntity(
            patientId = patientUuid,
            doctorNpiNumber = doctor.npiNumber,
            consultationFee = consultationFee,
            taxAmount = taxAmount,
            totalAmount = totalAmount,
            coPayAmount = coPayAmount,
            insurancePayableAmount = insurancePayableAmount,
            taxRatePercentage = billingConfiguration.taxRate * 100,
            coPayRatePercentage = billingConfiguration.coPayRate * 100,
            discountAmount = discountAmount,
            discountPercentage = discountPercentage
        )
    }

    private fun parsePatientId(patientId: String): UUID {
        try {
            return UUID.fromString(patientId)
        } catch (e: IllegalArgumentException) {
            throw HttpStatusException(HttpStatus.BAD_REQUEST, "Invalid patient ID format")
        }
    }

    private fun getPatient(patientId: UUID): PatientEntity {
        return patientRepository.findById(patientId).orElseThrow {
            HttpStatusException(HttpStatus.NOT_FOUND, "Patient not found with ID: $patientId")
        }
    }

    private fun getDoctor(doctorNpiNumber: String): Doctor {
        return doctorRepository.findById(doctorNpiNumber).orElseThrow {
            HttpStatusException(
                HttpStatus.NOT_FOUND,
                "Doctor not found with NPI number: $doctorNpiNumber"
            )
        }.toDomain()
    }

    private fun calculateConsultationFee(doctor: Doctor): Double {
        val experience = doctor.calculateYearsOfExperience()
        return doctor.specialty.getFeeByExperience(experience)
    }

    private fun calculateDiscountPercentage(patientId: UUID): Double {
        val priorAppointments = billRepository.countByPatientId(patientId)

        return minOf(
            priorAppointments.toDouble(),
            billingConfiguration.minDiscountRate * 100
        )
    }

    private fun calculateDiscountAmount(
        consultationFee: Double,
        discountPercentage: Double
    ): Double {
        return consultationFee * (discountPercentage / 100.0)
    }

    private fun calculateTax(amount: Double): Double {
        return amount * billingConfiguration.taxRate
    }

    private fun calculateCoPay(totalAmount: Double): Double {
        return totalAmount * billingConfiguration.coPayRate
    }
}
