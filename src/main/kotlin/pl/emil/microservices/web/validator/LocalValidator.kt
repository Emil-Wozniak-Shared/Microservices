package pl.emil.microservices.web.validator

import org.hibernate.validator.internal.engine.DefaultClockProvider.INSTANCE
import org.springframework.stereotype.Component
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import javax.validation.ClockProvider

@Component
object LocalValidator : LocalValidatorFactoryBean() {
    override fun getClockProvider(): ClockProvider = INSTANCE
}