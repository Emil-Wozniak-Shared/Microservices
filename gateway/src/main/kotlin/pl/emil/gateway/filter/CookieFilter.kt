package pl.emil.gateway.filter

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.core.env.Environment
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import pl.emil.gateway.config.X_AUTH
import reactor.core.publisher.Mono

@Component
class CookieFilter(
    private val environment: Environment
) : GatewayFilter {
    private val key = Keys.hmacShaKeyFor(environment.getProperty("token.secret")!!.toByteArray())

    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void> =
        exchange.request.run {
            if (!cookies.containsKey(X_AUTH)) {
                onError(exchange)
            }
            if (!cookies[X_AUTH]!![0].isJwtValid()) {
                onError(exchange)
            }
            chain.filter(exchange)
        }

    private fun onError(
        exchange: ServerWebExchange
    ): Mono<Void> {
        val response: ServerHttpResponse = exchange.response
        response.statusCode = HttpStatus.UNAUTHORIZED
        return response.setComplete()
    }

    private fun HttpCookie.isJwtValid(): Boolean {
        val subject = try {
            Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(this.value)
                .body
                .subject
        } catch (ex: Exception) {
            null
        }
        return !(subject == null || subject.isEmpty())
    }
}