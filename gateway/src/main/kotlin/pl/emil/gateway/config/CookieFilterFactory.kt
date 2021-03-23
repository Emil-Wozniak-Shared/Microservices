package pl.emil.gateway.config

import io.jsonwebtoken.Jwts
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class CookieFilterFactory(
    private val env: Environment,
) : AbstractGatewayFilterFactory<ConfigProps>(ConfigProps::class.java) {


    override fun apply(config: ConfigProps?): GatewayFilter =
        GatewayFilter { exchange, chain ->
            exchange.request.run {
                if (!cookies.containsKey(X_AUTH)) {
                    onError(exchange)
                }
                if (!cookies[X_AUTH]!![0].isJwtValid()) {
                    onError(exchange)
                }
                chain.filter(exchange)
            }
        }

    private fun onError(
        exchange: ServerWebExchange
    ): Mono<Void> {
        val response: ServerHttpResponse = exchange.response
        response.statusCode = UNAUTHORIZED
        return response.setComplete()
    }

    private fun HttpCookie.isJwtValid(): Boolean {
        val subject = try {
            Jwts
                .parser()
                .setSigningKey(env.getProperty("token.secret"))
                .parseClaimsJws(this.value)
                .body
                .subject
        } catch (ex: Exception) {
            null
        }
        return !(subject == null || subject.isEmpty())
    }
}