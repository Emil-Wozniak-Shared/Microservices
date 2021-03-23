package pl.emil.gateway.config

import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

//@Component
class CookiePreFilter : GlobalFilter {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain):
            Mono<Void> = chain.filter(exchange)
}