package pl.emil.posts.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.*
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import pl.emil.common.config.SecurityWebConfig
import pl.emil.posts.repo.BearerServerSecurityContextRepository
import pl.emil.posts.security.filter.JWTWebFilter
import pl.emil.posts.security.filter.CorsFilter
import pl.emil.posts.security.token.JwtAuthenticationProvider
import pl.emil.posts.security.token.JwtSigner

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurity(private val signer: JwtSigner): SecurityWebConfig {

    @Bean
    override fun springWebFilterChain(
        http: ServerHttpSecurity, authenticationManager: ReactiveAuthenticationManager
    ): SecurityWebFilterChain = http
        .apply {
            addFilterAt(CorsFilter(), FIRST)
            addFilterAt(JWTWebFilter(authenticationManager), AUTHENTICATION)
            securityContextRepository(BearerServerSecurityContextRepository())
            authorizeExchange()
                .pathMatchers("/yml").permitAll()
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/oauth/token").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/users").permitAll()
                .anyExchange().authenticated()
            httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .logout().disable()
        }
        .build()

    @Bean
    override fun reactiveAuthenticationManager(): ReactiveAuthenticationManager =
        JwtAuthenticationProvider(signer)

    @Bean
    override fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}

