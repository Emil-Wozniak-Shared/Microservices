package pl.emil.users.security.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.parserBuilder
import io.jsonwebtoken.SignatureAlgorithm.HS512
import io.jsonwebtoken.SignatureAlgorithm.RS256
import io.jsonwebtoken.security.Keys
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.time.Duration.ofMinutes
import java.time.Instant.now
import java.util.*

@Service
class JwtSigner(
    private val environment: Environment
) {

    val pair: KeyPair = Keys.keyPairFor(RS256)
    private val identity = "identity"

    @Suppress("DEPRECATION")
    fun createJwt(userId: String): String =
        Jwts.builder().apply {
            val expiredIn = Date.from(now().plus(ofMinutes(15)))
            val issuedAt = Date.from(now())
            signWith(HS512, environment.getProperty("token.secret"))
            setSubject(userId)
            setIssuer(identity)
            setExpiration(expiredIn)
            setIssuedAt(issuedAt)
        }.compact()

    /**
     * Validate the JWT where it will throw an exception if it isn't valid.
     */
    @Suppress("DEPRECATION")
    fun validateJwt(jwt: String): Jws<Claims> =
        Jwts
            .parser()
            .setSigningKey(environment.getProperty("token.secret"))
            .parseClaimsJws(jwt)
}