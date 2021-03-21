package pl.emil.users.security

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component(value = "authenticationManager")
class AuthenticationManager : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        if (authentication is JwtAuthenticationToken) {
            authentication.isAuthenticated = true
        }
        return Mono.just(authentication)
    }
}