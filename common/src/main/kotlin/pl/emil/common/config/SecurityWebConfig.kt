package pl.emil.common.config

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

interface SecurityWebConfig {

    fun springWebFilterChain(
        http: ServerHttpSecurity,
        authenticationManager: ReactiveAuthenticationManager,
    ): SecurityWebFilterChain

    fun reactiveAuthenticationManager(): ReactiveAuthenticationManager

    fun passwordEncoder(): PasswordEncoder
}
