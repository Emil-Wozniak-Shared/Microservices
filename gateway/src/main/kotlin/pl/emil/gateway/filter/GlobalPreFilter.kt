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
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void> = with(exchange) {
        with(request) {
            log.info("""
               Entering path: ${path.value()} 
               Headers: ${headers.entries}
            """.trimIndent())
        }

        chain.filter(this)
    }

    override fun getOrder(): Int = 8
}
