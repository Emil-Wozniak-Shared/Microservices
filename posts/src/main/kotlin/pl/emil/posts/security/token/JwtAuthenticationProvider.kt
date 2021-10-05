package pl.emil.posts.security.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import pl.emil.common.security.token.CommonJwtAuthenticationProvider
import pl.emil.posts.security.model.JWTAuthenticationToken

class JwtAuthenticationProvider(private val signer: JwtSigner) : CommonJwtAuthenticationProvider<JwtSigner, JWTAuthenticationToken>(signer) {
    override fun createToken(claims: Jws<Claims>): JWTAuthenticationToken = JWTAuthenticationToken(claims)
}
