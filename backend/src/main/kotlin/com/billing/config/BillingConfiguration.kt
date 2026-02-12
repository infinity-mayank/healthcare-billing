package com.billing.config

import io.micronaut.context.annotation.ConfigurationProperties
import jakarta.validation.constraints.PositiveOrZero

@ConfigurationProperties("billing")
class BillingConfiguration {

    @PositiveOrZero
    var taxRate: Double = 0.00

    @PositiveOrZero
    var coPayRate: Double = 0.00

    @PositiveOrZero
    var minDiscountRate: Double = 0.00

}