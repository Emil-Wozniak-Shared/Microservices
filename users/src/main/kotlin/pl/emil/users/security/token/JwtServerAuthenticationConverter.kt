package pl.emil.users.security.token

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtServerAuthenticationConverter : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange): Mono<Authentication> =
        Mono
            .justOrEmpty(exchange)
            .flatMap {
                val auth = it.request.cookies["X-Auth"]
                Mono.justOrEmpty(auth)
            }
            .filter { it.isNotEmpty() }
            .map { it[0].value }
            .map { UsernamePasswordAuthenticationToken(it, it) }
}