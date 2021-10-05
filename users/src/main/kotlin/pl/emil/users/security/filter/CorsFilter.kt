package pl.emil.users.security.filter

import org.springframework.http.HttpMethod.OPTIONS
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class CorsFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        exchange.response.headers.apply {
            add("Access-Control-Allow-Origin", "*")
            add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS")
            add("Access-Control-Allow-Headers", HEADERS)
        }
        return if (exchange.request.method == OPTIONS) {
            exchange.response.apply {
                headers.add("Access-Control-Max-Age", "1728000")
                statusCode = NO_CONTENT
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
