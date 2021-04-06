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
import pl.emil.gateway.config.unauthorized
import reactor.core.publisher.Mono

@Component
class HeaderGatewayFilterFactory(
    private val environment: Environment,
) : AbstractGatewayFilterFactory<HeaderGatewayFilterFactory.HeaderConfig>(HeaderConfig::class.java) {

    private val key = Keys.hmacShaKeyFor(environment.getProperty("token.secret")!!.toByteArray())
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun apply(config: HeaderConfig): GatewayFilter =
        OrderedGatewayFilter({ exchange, chain ->
            with(exchange.request) {
                if (path.value().contains(config.verifiedPath)) {
                    exchange.unauthorized(!headers.containsKey(AUTHORIZATION))
                    exchange.unauthorized(headers.getFirst(AUTHORIZATION)?.isJwtValid() == false)
                }
                chain
                    .filter(exchange)
                    .then(Mono.fromRunnable {
                        logger.info("HeaderGatewayFilterFactory ")
                    })
            }
        }, 2)


    private fun String.isJwtValid(): Boolean {
        val subject = try {
            Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(this.replace("Bearer", ""))
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