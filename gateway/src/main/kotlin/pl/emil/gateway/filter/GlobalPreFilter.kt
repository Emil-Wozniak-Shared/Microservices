package pl.emil.gateway.filter

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalPreFilter : GlobalFilter, Ordered {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun filter(
        exchange: ServerWebExchange, chain: GatewayFilterChain
    ): Mono<Void> = with(exchange) {
        with(request) {
            logger.info("Entering path: ${path.value()}")
            logger.info("${headers.entries}")
        }

        chain.filter(this)
    }

    override fun getOrder(): Int = 8
}