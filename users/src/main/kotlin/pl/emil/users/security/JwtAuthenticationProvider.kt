package pl.emil.users.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.SignatureException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import pl.emil.users.security.token.JwtSigner
import reactor.core.publisher.Mono

class JwtAuthenticationProvider(
    private val signer: JwtSigner
    ) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication> = try {
        val token = authentication!!.principal as String
        val claims = signer.validateJwt(token) //Jwts.parser().setSigningKey(key).parseClaimsJws(token)
        Mono.just(JWTAuthenticationToken(claims))
    } catch (e: SignatureException) {
        Mono.empty()
    }
}