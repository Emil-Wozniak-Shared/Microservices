package pl.emil.gateway.config

import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange

fun ServerWebExchange.unauthorized(condition: Boolean) {
    if (condition) {
        this.apply {
            response.statusCode = HttpStatus.UNAUTHORIZED
        }
    }
}