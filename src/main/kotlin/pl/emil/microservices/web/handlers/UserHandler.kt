package pl.emil.microservices.web.handlers

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import pl.emil.microservices.model.user.User
import pl.emil.microservices.repo.UserRepository
import pl.emil.microservices.util.pageRequest
import reactor.core.publisher.Mono
import java.net.URI

@Service
class UserHandler(private val repo: UserRepository) : ApiHandler<User> {
    override fun all(request: ServerRequest): Mono<ServerResponse> =
        pageRequest(request) {
            val content = repo.findAll()
            ok().body(Mono.just(content))
        }

    override fun getOne(request: ServerRequest): Mono<ServerResponse> =
        Mono.just(request)
            .flatMap {
                val id = it.pathVariable("id")
                val entity = repo.findById(id.toUUID())
                ok().contentType(it.mediaType()).body(entity)
            }

    override fun create(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono<User>()
            .flatMap(repo::save)
            .flatMap { created(URI.create(it.id.toString())).body<User>(it) }

    override fun update(request: ServerRequest): Mono<ServerResponse> {
        return ok().body(Mono.empty())
    }

    override fun delete(request: ServerRequest): Mono<ServerResponse> {
        return ok().body(Mono.empty())
    }
}