package pl.emil.users.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils.createAuthorityList
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import pl.emil.users.security.UserAuthenticationFilter

@Configuration
@EnableWebFluxSecurity
class WebSecurity {

    @Bean
    fun security(http: ServerHttpSecurity): SecurityWebFilterChain {
        val adminRole: Collection<GrantedAuthority> = createAuthorityList("ROLE_ADMIN")
        return http
            .authorizeExchange()
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
            .httpBasic()
            .and()
            .build()

    }

//    @Bean
//    fun userFilter() = UserAuthenticationFilter()

    @Bean
    fun passwordEncoder(): PasswordEncoder =
        createDelegatingPasswordEncoder()

    @Bean
    fun userDetailsService(): MapReactiveUserDetailsService {
        val user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build()
        val admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("password")
            .roles("USER", "ADMIN")
            .build()
        return MapReactiveUserDetailsService(user, admin)
    }
}