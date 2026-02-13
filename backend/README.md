# Healthcare Billing System - Backend

A healthcare billing management system built with Micronaut 4.10.7 and Kotlin. This backend API handles patient registration, doctor management, and automated billing calculations with tax, insurance calculations and discount calculations.

## Features

- **Patient Management**: Register and manage patient information with insurance details
- **Doctor Management**: Track healthcare providers with NPI numbers and specialties
  - Specialities include: Cardiology, Orthopedics
- **Automated Billing**: Generate bills with automatic calculations for:
  - Consultation fees
  - Discount percentages
  - Tax calculations
  - Co-pay amounts
  - Insurance payable amounts
- **H2 Database**: In-memory database for development and testing
- **RESTful API**: Clean REST endpoints with proper validation
- **Comprehensive Testing**: Unit Tests

## Tech Stack

- **Framework**: Micronaut 4.10.7
- **Language**: Kotlin 1.9.25
- **Build Tool**: Gradle with Kotlin DSL
- **JDK**: Java 21
- **Database**: H2 (in-memory)
- **Data Access**: Micronaut Data JDBC with HikariCP
- **Serialization**: Jackson with Kotlin module
- **Validation**: Jakarta Validation API
- **Testing**: JUnit 5, Mockito Kotlin
- **Processing**: Kotlin Symbol Processing (KSP)

## Prerequisites

- JDK 21
- Gradle (wrapper included)

## Getting Started

### Build the Project

```bash
./gradlew build
```

### Run the Application

```bash
./gradlew run
```

The server will start on `http://localhost:8080`

### Run Tests

```bash
./gradlew test
```

## API Endpoints

### Patients

- `POST /api/patients` - Register a new patient
- `GET /api/patients` - Get all patients

### Doctors

- `POST /api/doctors` - Register a new doctor
- `GET /api/doctors` - Get all doctors

### Billing

- `GET /api/bill/generate?patientId={id}&doctorNpiNumber={npi}` - Generate a bill
- `POST /api/bill/save` - Save a bill

### Health Check

- `GET /health` - Application health status

## Configuration

The application can be configured via `src/main/resources/application.yml`:

```yaml
billing:
  tax-rate: 0.12           # 12% tax rate
  co-pay-rate: 0.10        # 10% co-pay rate
  min-discount-rate: 0.10  # 10% minimum discount rate
```

## Database Schema

The application uses H2 database with the following tables:

- **doctors**: NPI number, name, specialty, practice start date
- **patients**: Patient details with insurance information (BIN, PCN, Member ID)
- **bills**: Complete billing records with all calculations

Schema is automatically initialized from `src/main/resources/schema.sql`

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── kotlin/com/billing/
│   │   │   ├── controller/     # REST controllers
│   │   │   ├── service/        # Business logic
│   │   │   ├── repository/     # Data access layer
│   │   │   ├── entity/         # Database entities
│   │   │   ├── model/          # Domain models
│   │   │   └── dto/            # Data transfer objects
│   │   └── resources/
│   │       ├── application.yml # Application config
│   │       └── schema.sql      # Database schema
│   └── test/                   # Unit and integration tests
├── build.gradle.kts            # Build configuration
└── README.md
```

## Testing

The project includes comprehensive tests:

- Controller tests with HTTP client
- Service layer unit tests with Mockito
- Repository integration tests
- Model validation tests

## Assumptions

- No authentication or authorization implemented for any API.
- Using Bill as an appointment for simplicity.

