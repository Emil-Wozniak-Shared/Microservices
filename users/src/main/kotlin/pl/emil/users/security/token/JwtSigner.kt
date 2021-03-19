package pl.emil.users.security.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.parserBuilder
import io.jsonwebtoken.SignatureAlgorithm.PS512
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.time.Duration.ofMinutes
import java.time.Instant.now
import java.util.*

@Service
class JwtSigner {
    val keyPair: KeyPair = Keys.keyPairFor(PS512)
    private val identity = "identity"

    fun createJwt(userId: String): String =
        with(Jwts.builder()) {
            val expiredIn = Date.from(now().plus(ofMinutes(15)))
            val issuedAt = Date.from(now())
            signWith(keyPair.private, PS512)
            setSubject(userId)
            setIssuer(identity)
            setExpiration(expiredIn)
            setIssuedAt(issuedAt)
        }.compact()

    /**
     * Validate the JWT where it will throw an exception if it isn't valid.
     */
    fun validateJwt(jwt: String): Jws<Claims> =
        parserBuilder()
            .setSigningKey(keyPair.public)
            .build()
            .parseClaimsJws(jwt)
}