package pl.emil.microservices.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

import org.springframework.context.annotation.Primary
import org.springframework.validation.Validator


@Configuration
class AppConfig {

    @Bean
    @Primary
    fun springValidator(): Validator {
        return LocalValidatorFactoryBean()
    }
}