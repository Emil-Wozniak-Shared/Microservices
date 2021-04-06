package pl.emil.users.security.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.HS512
import io.jsonwebtoken.security.Keys
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.time.Duration.ofMinutes
import java.time.Instant.now
import java.util.*

@Service
class JwtSigner(
    private val environment: Environment
) {

    private val identity = "identity"
    private val key = Keys.hmacShaKeyFor(environment.getProperty("token.secret")!!.toByteArray())

    fun createJwt(userId: String): String =
        Jwts.builder().apply {
            val expiredIn = Date.from(now().plus(ofMinutes(15)))
            val issuedAt = Date.from(now())
            signWith(key, HS512) //, environment.getProperty("token.secret"))
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