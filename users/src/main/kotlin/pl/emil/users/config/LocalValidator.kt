package pl.emil.users.config

import org.hibernate.validator.internal.engine.DefaultClockProvider
import org.springframework.stereotype.Component
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import javax.validation.ClockProvider

@Component
object LocalValidator : LocalValidatorFactoryBean() {
    override fun getClockProvider(): ClockProvider = DefaultClockProvider.INSTANCE
}