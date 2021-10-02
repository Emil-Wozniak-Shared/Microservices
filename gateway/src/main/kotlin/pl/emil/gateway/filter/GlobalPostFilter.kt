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
    private val log: Logger = LoggerFactory.getLogger(this::class.java.simpleName)

    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void> = with(exchange) {
        chain.filter(this).then(Mono.fromRunnable {
            log.info("""
                Exiting path: ${request.path.value()}
                Request 
                => headers: ${request.headers.map { it }.toList()}
                => cookies: ${request.cookies.map { it }.toList()}
                Response
                => status:  ${response.statusCode}
                => headers: ${response.headers.map { "${it.key}: ${it.value}" }.toList()}
            """.trimIndent()
            )
        })
    }

    override fun getOrder(): Int = 7
}
