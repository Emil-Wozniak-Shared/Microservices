package pl.emil.users.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.context.ServerSecurityContextRepository

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurity(
    private val authenticationManager: ReactiveAuthenticationManager,
    private val securityContextRepository: ServerSecurityContextRepository
) {

    @Bean
    fun security(
        http: ServerHttpSecurity,
        jwtAuthenticationManager: ReactiveAuthenticationManager,
        jwtAuthenticationConverter: ServerAuthenticationConverter
    ): SecurityWebFilterChain = http
        .csrf().disable()
        .authorizeExchange()
        .pathMatchers(POST, "/api/users").permitAll()
        .pathMatchers("/api/signup").permitAll()
        .pathMatchers("/api/login").permitAll()
        .pathMatchers("/api/**").permitAll()
        .and()
        .addFilterAt(
            AuthenticationWebFilter(jwtAuthenticationManager).apply {
                setServerAuthenticationConverter(jwtAuthenticationConverter)
            }, AUTHENTICATION
        )
        .httpBasic().disable()
        .formLogin().disable()
        .logout().disable()
        .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder = createDelegatingPasswordEncoder()
}