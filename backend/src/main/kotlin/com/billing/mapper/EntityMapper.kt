package com.billing.mapper

import com.billing.entity.DoctorEntity
import com.billing.entity.PatientEntity
import com.billing.model.Doctor
import com.billing.model.Insurance
import com.billing.model.Patient
import com.billing.model.Specialty

object EntityMapper {

    fun DoctorEntity.toDomain(): Doctor {
        return Doctor(
            npiNumber = this.npiNumber,
            firstName = this.firstName,
            lastName = this.lastName,
            specialty = Specialty.valueOf(this.specialty),
            practiceStartDate = this.practiceStartDate
        )
    }

    fun Doctor.toEntity(): DoctorEntity {
        return DoctorEntity(
            npiNumber = this.npiNumber,
            firstName = this.firstName,
            lastName = this.lastName,
            specialty = this.specialty.name,
            practiceStartDate = this.practiceStartDate
        )
    }

    fun PatientEntity.toDomain(): Patient {
        return Patient(
            id = this.id.toString(),
            firstName = this.firstName,
            lastName = this.lastName,
            dateOfBirth = this.dateOfBirth,
            insurance = Insurance(
                binNumber = this.insuranceBIN,
                pcnNumber = this.insurancePCN,
                memberId = this.insuranceMemberID
            )
        )
    }

    fun Patient.toEntity(): PatientEntity {
        return PatientEntity(
            firstName = this.firstName,
            lastName = this.lastName,
            dateOfBirth = this.dateOfBirth,
            insuranceBIN = this.insurance.binNumber,
            insurancePCN = this.insurance.pcnNumber,
            insuranceMemberID = this.insurance.memberId
        )
    }
}

