package pl.emil.users.security.filter

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class JWTWebFilter(
    private val authenticationManager: ReactiveAuthenticationManager
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        ReactiveSecurityContextHolder.getContext()
            .flatMap { securityContext ->
                this.authenticationManager.authenticate(securityContext.authentication)
                    .map {
                        securityContext.authentication = it
                    }
                    .map {
                        exchange
                    }
            }
            .defaultIfEmpty(exchange)
            .flatMap {
                chain.filter(it)
            }
}

