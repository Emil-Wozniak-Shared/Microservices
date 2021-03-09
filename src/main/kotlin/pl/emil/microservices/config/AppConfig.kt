package pl.emil.microservices.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.validation.Validator

@Configuration
@EnableJpaRepositories("pl.emil.microservices.repo")
class AppConfig {

    @Bean
    @Primary
    fun springValidator(): Validator = LocalValidatorFactoryBean()
}