package pl.emil.users.security

import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.OPTIONS
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class JWTWebFilter(
    private val authenticationManager: ReactiveAuthenticationManager
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap { securityContext ->
                this.authenticationManager.authenticate(securityContext.authentication)
                    .map { securityContext.authentication = it }
                    .map { exchange }
            }
            .defaultIfEmpty(exchange)
            .flatMap { chain.filter(it) }
    }
}

class CorsFilter : WebFilter {
    override fun filter(ctx: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        ctx.response.headers.apply {
            add("Access-Control-Allow-Origin", "*")
            add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS")
            add("Access-Control-Allow-Headers", HEADERS)
        }
        return if (ctx.request.method == OPTIONS) {
            ctx.response.apply {
                headers.add("Access-Control-Max-Age", "1728000")
                statusCode = NO_CONTENT
            }
            Mono.empty()
        } else {
            ctx.response.headers.add("Access-Control-Expose-Headers", HEADERS)
            chain.filter(ctx)
        }
    }

    companion object {
        const val HEADERS = "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With," +
                "If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,Authorization"
    }

}