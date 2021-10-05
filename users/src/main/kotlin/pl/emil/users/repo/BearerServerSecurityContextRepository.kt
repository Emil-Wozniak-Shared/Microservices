package pl.emil.users.repo

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder.createEmptyContext
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class BearerServerSecurityContextRepository : ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange, context: SecurityContext?): Mono<Void> =
        Mono.empty()

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> =
        with(createEmptyContext()) {
            tokenFromRequest(exchange.request)?.let { token ->
                this.authentication = PreAuthenticatedAuthenticationToken(token, token)
                Mono.just(this)
            } ?: Mono.empty()
        }

    private fun tokenFromRequest(request: ServerHttpRequest): String? {
        if (request.headers["Authorization"] == null) return null
        val value = (request.headers["Authorization"] as List<String>)[0]
        if (!value.lowercase().startsWith("bearer")) {
            return null
        }
        val parts = value
            .split(" ".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        return if (parts.size < 2) {
            null
        } else parts[1].trim { it <= ' ' }
    }
}
