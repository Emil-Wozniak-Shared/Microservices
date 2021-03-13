package pl.emil.users.web

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import pl.emil.users.config.ApiHandler
import pl.emil.users.config.id
import pl.emil.users.model.User
import pl.emil.users.service.UserService
import reactor.core.publisher.Mono

@Service
class UserHandler(
    private val service: UserService
) : ApiHandler<User> {
    override fun all(request: ServerRequest): Mono<ServerResponse> =
        ok().body(service.all())

    override fun getOne(request: ServerRequest): Mono<ServerResponse> =
        ok().body(service.one(request.id()))

    override fun create(request: ServerRequest): Mono<ServerResponse> = ok().body(service.create(request.bodyToMono()))

    override fun update(request: ServerRequest): Mono<ServerResponse> = badRequest().build()

    override fun delete(request: ServerRequest): Mono<ServerResponse> = badRequest().build()
}