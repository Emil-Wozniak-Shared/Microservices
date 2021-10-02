package pl.emil.gateway.factory

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import pl.emil.gateway.config.BEARER
import pl.emil.gateway.config.unauthorized
import pl.emil.gateway.utils.isNotAuthorized
import pl.emil.gateway.utils.notContainsKey
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.*

@Component
class HeaderGatewayFilterFactory(private val environment: Environment) :
    AbstractGatewayFilterFactory<HeaderGatewayFilterFactory.HeaderConfig>(HeaderConfig::class.java) {

    private val key = Keys.hmacShaKeyFor(environment.getProperty("token.secret")!!.toByteArray())
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun apply(config: HeaderConfig): GatewayFilter =
        OrderedGatewayFilter({ exchange, chain ->
            with(exchange) {
                request.run {
                    if (path.value().contains(config.verifiedPath)) {
                        unauthorized(headers.isNotAuthorized())
                        unauthorized(headers.getFirst(AUTHORIZATION)?.isJwtValid() == false)
                    }
                }
                chain
                    .filter(exchange)
                    .then(fromRunnable {
                        logger.info(this::class.java.simpleName)
                    })
            }
        }, 2)


    private fun String.isJwtValid(): Boolean {
        val subject = try {
            Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(this.replace(BEARER, ""))
                .body
                .subject
        } catch (ex: Exception) {
            null
        }
        return !(subject == null || subject.isEmpty())
    }

    class HeaderConfig(
        var verifiedPath: String
    )
}
