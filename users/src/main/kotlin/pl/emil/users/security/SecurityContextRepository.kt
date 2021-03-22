package pl.emil.users.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository : ServerSecurityContextRepository {

    override fun save(serverWebExchange: ServerWebExchange, securityContext: SecurityContext): Mono<Void> {
        return Mono.empty()
    }

    override fun load(serverWebExchange: ServerWebExchange): Mono<SecurityContext> =
        ReactiveSecurityContextHolder.getContext().map {
            with(it.authentication) {
                UsernamePasswordAuthenticationToken(principal, credentials, authorities)
                SecurityContextImpl(this)
            }
        }
}