package pl.emil.common.security.filter

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

abstract class CommonJWTWebFilter(private val authenticationManager: ReactiveAuthenticationManager) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        ReactiveSecurityContextHolder.getContext()
            .flatMap { context ->
                authenticationManager.authenticate(context.authentication)
                    .map { authentication -> context.authentication = authentication }
                    .map { exchange }
            }
            .defaultIfEmpty(exchange)
            .flatMap { chain.filter(it) }
}
