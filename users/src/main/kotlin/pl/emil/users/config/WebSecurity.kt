package pl.emil.users.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurity {

    @Bean
    fun security(
        http: ServerHttpSecurity,
        jwtAuthenticationManager: ReactiveAuthenticationManager,
        jwtAuthenticationConverter: ServerAuthenticationConverter
    ): SecurityWebFilterChain {
        val authenticationWebFilter = AuthenticationWebFilter(jwtAuthenticationManager).apply {
            setServerAuthenticationConverter(jwtAuthenticationConverter)
        }
        return http
            .csrf().disable()
            .authorizeExchange()
            .pathMatchers("/api/signup").permitAll()
            .pathMatchers("/api/login").permitAll()
            .pathMatchers("/api/**").authenticated()
            .and()
            .addFilterAt(authenticationWebFilter, AUTHENTICATION)
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = createDelegatingPasswordEncoder()
}