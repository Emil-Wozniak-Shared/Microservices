package pl.emil.users.security.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.security.SignatureException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import pl.emil.common.security.model.CommonJWTToken
import pl.emil.common.security.token.CommonJwtAuthenticationProvider
import pl.emil.users.security.model.JWTAuthenticationToken
import reactor.core.publisher.Mono

class JwtAuthenticationProvider(private val signer: JwtSigner) : CommonJwtAuthenticationProvider<JwtSigner, JWTAuthenticationToken>(signer) {
    override fun createToken(claims: Jws<Claims>): JWTAuthenticationToken = JWTAuthenticationToken(claims)
}
