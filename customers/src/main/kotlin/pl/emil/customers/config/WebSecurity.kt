package pl.emil.customers.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.*
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurity {
    @Bean
    fun springWebFilterChain(
        http: ServerHttpSecurity,
    ): SecurityWebFilterChain = http
        .apply {
            addFilterAt(CorsFilter(), FIRST)
            authorizeExchange()
                .pathMatchers("/api").permitAll()
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/oauth/token").permitAll()
                .pathMatchers(GET, "/api/customers").permitAll()
                .anyExchange().authenticated()
            httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .logout().disable()
        }
        .build()
}
