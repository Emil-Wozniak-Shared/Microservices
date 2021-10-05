package pl.emil.common.security.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.core.env.Environment
import java.time.Duration
import java.time.Instant
import java.util.*

abstract class CommonJwtSigner(
    private val environment: Environment
) {

    private val identity = "identity"
    private val key = Keys.hmacShaKeyFor(environment.getProperty("token.secret")!!.toByteArray())

    fun createJwt(userId: String): String =
        Jwts.builder().apply {
            val expiredIn = Date.from(Instant.now().plus(Duration.ofMinutes(15)))
            val issuedAt = Date.from(Instant.now())
            signWith(key, SignatureAlgorithm.HS512) //, environment.getProperty("token.secret"))
            setSubject(userId)
            setIssuer(identity)
            setExpiration(expiredIn)
            setIssuedAt(issuedAt)
        }.compact()

    /**
     * Validate the JWT where it will throw an exception if it isn't valid.
     */
    fun validateJwt(jwt: String): Jws<Claims> =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwt)
}
