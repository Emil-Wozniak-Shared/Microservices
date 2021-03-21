package pl.emil.gateway.config

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebSession


@Component
class CookieFilterFactory : AbstractGatewayFilterFactory<Any>() {

    override fun apply(config: Any?): GatewayFilter =
        GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            exchange.session.map { obj: WebSession -> obj.save() }
                .then(chain.filter(exchange))
        }
}