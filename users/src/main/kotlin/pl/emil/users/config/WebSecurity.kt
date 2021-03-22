package pl.emil.users.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.FIRST
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import pl.emil.users.repo.BearerServerSecurityContextRepository
import pl.emil.users.security.CorsFilter
import pl.emil.users.security.JWTWebFilter
import pl.emil.users.security.JwtAuthenticationProvider
import pl.emil.users.security.token.JwtSigner

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurity(
    private val signer: JwtSigner
) {

    @Bean
    fun springWebFilterChain(
        http: ServerHttpSecurity, authenticationManager: ReactiveAuthenticationManager
    ): SecurityWebFilterChain {
        http.addFilterAt(CorsFilter(), FIRST)
        http.addFilterAt(JWTWebFilter(authenticationManager), AUTHENTICATION)
        http.securityContextRepository(BearerServerSecurityContextRepository())

        http.authorizeExchange()
            .pathMatchers("/actuator/**").permitAll()
            .pathMatchers("/oauth/token").permitAll()
            .anyExchange().authenticated()

        http.httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .logout().disable()

        return http.build()
    }

    @Bean
    fun reactiveAuthenticationManager(): ReactiveAuthenticationManager =
        JwtAuthenticationProvider(signer)

//
//        @Bean
//    fun security(
//        http: ServerHttpSecurity,
//        jwtAuthenticationManager: ReactiveAuthenticationManager,
//        jwtAuthenticationConverter: ServerAuthenticationConverter
//    ): SecurityWebFilterChain = http
//        .csrf().disable()
//        .authorizeExchange()
//        .pathMatchers(POST, "/api/users").permitAll()
//        .pathMatchers("/api/signup").permitAll()
//        .pathMatchers("/api/login").permitAll()
//        .pathMatchers("/api/**").permitAll()
//        .and()
//        .addFilterAt(
//            AuthenticationWebFilter(jwtAuthenticationManager).apply {
//                setServerAuthenticationConverter(jwtAuthenticationConverter)
//            }, AUTHENTICATION
//        )
//        .httpBasic().disable()
//        .formLogin().disable()
//        .logout().disable()
//        .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder = createDelegatingPasswordEncoder()
}