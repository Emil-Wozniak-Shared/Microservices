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
class GlobalPostFilter : GlobalFilter, Ordered {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun filter(
        exchange: ServerWebExchange, chain: GatewayFilterChain
    ): Mono<Void> = with(exchange) {
        chain.filter(this).then(Mono.fromRunnable {
            with(request) {
                logger.info("Exiting path: ${path.value()}")
            }
        })
    }

    override fun getOrder(): Int = 7
}