package pl.emil.users.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils.createAuthorityList
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter

val adminRole: Collection<GrantedAuthority> = createAuthorityList("ROLE_ADMIN")

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
        val authenticationWebFilter = AuthenticationWebFilter(jwtAuthenticationManager)
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter)
        return http
            .csrf().disable()
            .authorizeExchange()
            .pathMatchers("/authenticate/*").permitAll()
            .pathMatchers("/api/*").permitAll()
            .pathMatchers("/encoder").permitAll()
            .pathMatchers("/hello").permitAll()
            .pathMatchers("/message").hasRole("USER")
            .pathMatchers("/users/{username}")
            .access { mono, context ->
                mono
                    .map { auth ->
                        auth.authorities.containsAll(adminRole)
                            .or(auth.name == context.variables["username"])
                    }
                    .map { AuthorizationDecision(it) }
            }
            .anyExchange().authenticated()
            .and()
            .addFilterAt(authenticationWebFilter, AUTHENTICATION)
            .httpBasic()
            .disable()
            .csrf()
            .disable()
            .formLogin()
            .disable()
            .logout()
            .disable()
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder =
        createDelegatingPasswordEncoder()

    @Bean
    fun userDetailsService(): MapReactiveUserDetailsService {
        val user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build()
        val admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("password"))
            .roles("USER", "ADMIN")
            .build()
        return MapReactiveUserDetailsService(user, admin)
    }
}