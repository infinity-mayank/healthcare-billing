package com.billing.config

import io.micronaut.context.annotation.ConfigurationProperties
import jakarta.validation.constraints.Positive

@ConfigurationProperties("billing")
class BillingConfiguration {

    @Positive
    var taxRate: Double = 0.00

}