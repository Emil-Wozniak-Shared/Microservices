package pl.emil.users.web

import org.springframework.http.MediaType.APPLICATION_XML
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import pl.emil.users.config.*
import pl.emil.users.model.User
import pl.emil.users.repo.UserRepository
import pl.emil.users.service.UserService
import reactor.core.publisher.Mono
import java.net.URI
import java.time.LocalDateTime.now

@Service
class UserHandler(
    private val service: UserService,
    private val repo: UserRepository,
    private val validator: RequestValidator,
    private var encoder: PasswordEncoder
) : ApiHandler<User> {
    override fun all(request: ServerRequest): Mono<ServerResponse> {
        val contentType = request.headers().contentType()
        return if (contentType.isPresent && contentType.get() == APPLICATION_XML) {
            val all = service.allAsXML()
            ok().mediaType(request).body(all)
        } else ok().body(service.all())
    }

    override fun getOne(request: ServerRequest): Mono<ServerResponse> =
        service
            .one(request.id())
            .flatMap {
                ok().mediaType(request).bodyValue(it)
            }

    override fun create(request: ServerRequest): Mono<ServerResponse> =
        request
            .validateBody<User>(validator, "email", "firstName", "lastName")
            .doOnNext { user ->
                user.createdAt = now()
                this.repo.save(user).subscribe()
            }
            .flatMap {
                created(URI.create("/users/${it.id}")).build()
            }

    override fun update(request: ServerRequest): Mono<ServerResponse> =
        badRequest().mediaType(request).build()

    override fun delete(request: ServerRequest): Mono<ServerResponse> =
        badRequest().mediaType(request).build()

    fun encoder(request: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(encoder.encode("password")))

    fun anonymous(request: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just("Hello!"))

    fun message(serverRequest: ServerRequest): Mono<ServerResponse> =
        ok().body(serverRequest.principal().map { "Hello, " + it.name + "!" })

    fun username(request: ServerRequest): Mono<ServerResponse> =
        ok()
            .body(request
                .principal()
                .map { p ->
                    UserDetails::class.java.cast(Authentication::class.java.cast(p)
                        .principal) }
            )

}