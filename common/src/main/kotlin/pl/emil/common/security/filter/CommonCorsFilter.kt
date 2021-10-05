package pl.emil.common.security.filter

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

interface CommonCorsFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        exchange.response.headers.apply {
            add("Access-Control-Allow-Origin", "*")
            add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS")
            add("Access-Control-Allow-Headers", HEADERS)
        }
        return if (exchange.request.method == HttpMethod.OPTIONS) {
            exchange.response.apply {
                headers.add("Access-Control-Max-Age", "1728000")
                statusCode = HttpStatus.NO_CONTENT
            }
            Mono.empty()
        } else {
            exchange.response.headers.add("Access-Control-Expose-Headers", HEADERS)
            chain.filter(exchange)
        }
    }

    companion object {
        const val HEADERS = "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With," +
                "If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,Authorization"
    }
}
