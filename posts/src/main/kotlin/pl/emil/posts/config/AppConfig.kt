package pl.emil.posts.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.validation.Validator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class AppConfig {

    @Bean
    @Primary
    fun springValidator(): Validator = LocalValidatorFactoryBean()
}