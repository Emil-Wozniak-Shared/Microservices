package pl.emil.users.security.token

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import pl.emil.users.security.JwtAuthenticationToken
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationManager(private val signer: JwtSigner) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> =
        Mono.just(authentication)
            .map {
                val jwt = signer.validateJwt(it.credentials as String)
                JwtAuthenticationToken(
                    jwt.body.subject,
                    authentication.credentials as String,
                    mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
                )
            }
}