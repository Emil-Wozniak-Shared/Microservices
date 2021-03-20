package pl.emil.gateway.config

import io.jsonwebtoken.Jwts
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import pl.emil.gateway.config.AuthorizationCookieFilter.Config

@Component
class AuthorizationCookieFilter(
    private val environment: Environment
) : AbstractGatewayFilterFactory<Config>() {
    private val noXAuthMsg = "No X-Auth Cookie"
    private val noXAuthBodyMsg ="No X-Auth body"

    inner class Config

    override fun apply(config: Config): GatewayFilter =
        GatewayFilter { exchange, chain ->
            val request = exchange.request
            val cookies = request.cookies
            cookies.let { cookie ->
                if (cookie.containsKey(X_AUTH)) {
                    onError(exchange, noXAuthMsg, UNAUTHORIZED)
                } else cookie[X_AUTH]!![0]!!.value.let { jwt ->
                    if (jwt.isNotValid()) {
                        onError(exchange, noXAuthBodyMsg, UNAUTHORIZED)
                    }
                }
            }
            chain.filter(exchange)
        }

    /**
     * Checks if Token's body claims is null or empty.
     * @return false if body subject is not null or not empty,
     * otherwise true
     */
    private fun String.isNotValid(): Boolean =
        Jwts.parserBuilder()
            .setSigningKey(environment.getProperty("token.secret"))
            .build()
            .parseClaimsJws(this).body.subject.isNullOrBlank()

    private fun onError(
        exchange: ServerWebExchange,
        message: String,
        status: HttpStatus
    ) {
        exchange.response.apply {
            statusCode = status
            setComplete()
        }
    }
}