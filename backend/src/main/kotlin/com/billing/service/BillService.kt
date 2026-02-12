package com.billing.service

import com.billing.config.BillingConfiguration
import com.billing.dto.BillResponse
import com.billing.dto.SaveBillRequest
import com.billing.dto.SaveBillResponse
import com.billing.entity.BillEntity
import com.billing.mapper.EntityMapper.toDomain
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
        val patientId = try {
            UUID.fromString(patientId)
        } catch (e: IllegalArgumentException) {
            throw HttpStatusException(HttpStatus.BAD_REQUEST, "Invalid patient ID format")
        }

        val patientEntity = patientRepository.findById(patientId).orElseThrow {
            HttpStatusException(HttpStatus.NOT_FOUND, "Patient not found with ID: $patientId")
        }

        val doctorEntity = doctorRepository.findById(doctorNpiNumber).orElseThrow {
            HttpStatusException(HttpStatus.NOT_FOUND, "Doctor not found with NPI number: $doctorNpiNumber")
        }

        val doctor = doctorEntity.toDomain()
        val yearsOfExperience = doctor.calculateYearsOfExperience()

        val consultationFee = doctor.specialty.getFeeByExperience(yearsOfExperience)

        val taxAmount = consultationFee * billingConfiguration.taxRate

        val totalAmount = consultationFee + taxAmount

        val coPayAmount = billingConfiguration.coPayRate * totalAmount

        val insurancePayableAmount = totalAmount - coPayAmount

        return BillResponse(
            patientId = patientEntity.id.toString(),
            doctorNpiNumber = doctorEntity.npiNumber,
            consultationFee = consultationFee,
            taxAmount = taxAmount,
            totalAmount = totalAmount,
            coPayAmount = coPayAmount,
            insurancePayableAmount = insurancePayableAmount,
            taxRatePercentage = billingConfiguration.taxRate * 100,
            coPayRatePercentage = billingConfiguration.coPayRate * 100
        )
    }

    fun saveBill(request: SaveBillRequest): SaveBillResponse {
        val patientId = try {
            UUID.fromString(request.patientId)
        } catch (e: IllegalArgumentException) {
            throw HttpStatusException(HttpStatus.BAD_REQUEST, "Invalid patient ID format")
        }

        patientRepository.findById(patientId).orElseThrow {
            HttpStatusException(HttpStatus.NOT_FOUND, "Patient not found with ID: ${request.patientId}")
        }

        doctorRepository.findById(request.doctorNpiNumber).orElseThrow {
            HttpStatusException(HttpStatus.NOT_FOUND, "Doctor not found with NPI number: ${request.doctorNpiNumber}")
        }

        val billEntity = BillEntity(
            patientId = patientId,
            doctorNpiNumber = request.doctorNpiNumber,
            consultationFee = request.consultationFee,
            taxAmount = request.taxAmount,
            totalAmount = request.totalAmount,
            coPayAmount = request.coPayAmount,
            insurancePayableAmount = request.insurancePayableAmount,
            taxRatePercentage = request.taxRatePercentage,
            coPayRatePercentage = request.coPayRatePercentage
        )

        val savedBill = billRepository.save(billEntity)

        return SaveBillResponse(
            id = savedBill.id.toString(),
            createdAt = billEntity.createdAt.toString()
        )
    }
}
