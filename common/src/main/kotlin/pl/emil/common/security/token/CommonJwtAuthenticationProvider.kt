package pl.emil.common.security.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.security.SignatureException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import pl.emil.common.security.model.CommonJWTToken
import reactor.core.publisher.Mono

abstract class CommonJwtAuthenticationProvider<S: CommonJwtSigner,T : CommonJWTToken>(private val signer: S) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication?> =
        try {
            val token = authentication?.principal as String?
            val claims = token?.let { signer.validateJwt(it) } //Jwts.parser().setSigningKey(key).parseClaimsJws(token)
            if (claims != null) Mono.just(createToken(claims))
            else Mono.error(UsernameNotFoundException("No user"))
        } catch (e: SignatureException) {
            Mono.empty()
        }

    abstract fun  createToken(claims: Jws<Claims>): T
}
