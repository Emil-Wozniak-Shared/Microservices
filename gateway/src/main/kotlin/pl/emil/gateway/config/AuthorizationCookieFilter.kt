package pl.emil.gateway.config

import io.jsonwebtoken.Jwts
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthorizationCookieFilter(
    private val environment: Environment
) : GatewayFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void> = exchange.request.run {
        if (!headers.containsKey(AUTHORIZATION)) {
            return onError(exchange)
        }
        val authorizationHeader: String = headers[AUTHORIZATION]!![0]
        if (!isJwtValid(authorizationHeader.replace("Bearer", ""))) {
            return onError(exchange)
        }
        return chain.filter(exchange)
    }


    private fun onError(
        exchange: ServerWebExchange
    ): Mono<Void> {
        val response: ServerHttpResponse = exchange.response
        response.statusCode = UNAUTHORIZED
        return response.setComplete()
    }

    private fun isJwtValid(jwt: String): Boolean {
        var returnValue = true
        var subject: String? = null
        try {
            subject = Jwts
                .parser()
                .setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(jwt)
                .body
                .subject
        } catch (ex: Exception) {
            returnValue = false
        }
        if (subject == null || subject.isEmpty()) {
            returnValue = false
        }
        return returnValue
    }
}
//
//    override fun filter(
//        exchange: ServerWebExchange,
//        chain: GatewayFilterChain
//    ): Mono<Void> =
//        exchange.run {
//            if (request.cookies.containsKey(X_AUTH)) onError(exchange)
//            request.cookies[X_AUTH]?.get(0)?.value?.let {
//                if (it.isNotValid()) onError(this)
//                else chain.filter(this)
//            }
//            chain.filter(this)
//        }
//
//    /**
//     * Checks if Token's body claims is null or empty.
//     * @return false if body subject is not null or not empty,
//     * otherwise true
//     */
//    @Suppress("DEPRECATION")
//    private fun String.isNotValid(): Boolean =
//        !Jwts
//            .parser()
//            .setSigningKey(environment.getProperty("token.secret"))
//            .parseClaimsJws(this)
//            .body.subject
//            .isNullOrEmpty()
//
//    private fun onError(exchange: ServerWebExchange) {
//        exchange.response.apply {
//            statusCode = UNAUTHORIZED
//        }.setComplete()
//    }
