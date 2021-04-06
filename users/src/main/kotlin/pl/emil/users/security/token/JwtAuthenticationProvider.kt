package pl.emil.users.security.token

import io.jsonwebtoken.security.SignatureException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import pl.emil.users.security.model.JWTAuthenticationToken
import reactor.core.publisher.Mono

class JwtAuthenticationProvider(
    private val signer: JwtSigner
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication?> = try {
        val token = authentication?.principal as String?
        val claims = token?.let { signer.validateJwt(it) } //Jwts.parser().setSigningKey(key).parseClaimsJws(token)
        if (claims != null) Mono.just(JWTAuthenticationToken(claims))
        else Mono.error(UsernameNotFoundException("No user"))
    } catch (e: SignatureException) {
        Mono.empty()
    }
}