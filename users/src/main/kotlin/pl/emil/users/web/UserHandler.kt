package pl.emil.users.web

import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_XML
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseCookie.fromClientResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import pl.emil.users.config.*
import pl.emil.users.config.exception.UserCreateException
import pl.emil.users.model.User
import pl.emil.users.model.UserCredentials
import pl.emil.users.security.token.JwtSigner
import pl.emil.users.service.UserService
import reactor.core.publisher.Mono
import java.net.URI

@Service
class UserHandler(
    private val service: UserService,
    private val validator: RequestValidator,
    private val signer: JwtSigner
) : ApiHandler<User> {

    private val users: MutableMap<String, UserCredentials> = mutableMapOf(
        "email@example.com" to UserCredentials("email@example.com", "pw")
    )

    override fun all(request: ServerRequest): Mono<ServerResponse> {
        val contentType = request.headers().contentType()
        return if (contentType.isPresent && contentType.get() == APPLICATION_XML) {
            val all = service.allAsXML()
            ok().mediaType(request).body(all)
        } else ok().body(service.all())
    }

    fun login(request: ServerRequest): Mono<ServerResponse> =
        request
            .validateBody<UserCredentials>(validator, "email", "password")
            .flatMap {
                val jwt = signer.createJwt(it.email)
                val authCookie = fromClientResponse("X-Auth", jwt)
                    .maxAge(3600)
                    .httpOnly(true)
                    .path("/")
                    .secure(false) // should be true in production
                    .build()

                noContent()
                    .header("Set-Cookie", authCookie.toString())
                    .build()
            }
            .flatMap { noContent().build() }
            .switchIfEmpty(status(UNAUTHORIZED).build())

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
                service.create(user).subscribe()
            }
            .flatMap {
                if (it == null) error(UserCreateException("That user cant't be created"))
                else created(URI.create("/users/${it.id}")).build()
            }

    override fun update(request: ServerRequest): Mono<ServerResponse> =
        badRequest().mediaType(request).build()

    override fun delete(request: ServerRequest): Mono<ServerResponse> =
        badRequest().mediaType(request).build()

    fun encoder(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono<String>().flatMap {
            ok().body(Mono.just(service.encode(it)))
        }

    fun message(request: ServerRequest): Mono<ServerResponse> =
        request.principal().flatMap {
            ok().body(Mono.just("Hello, ${it.name} !"))
        }

    fun username(request: ServerRequest): Mono<ServerResponse> =
        ok()
            .body(request
                .principal()
                .map { p ->
                    UserDetails::class.java.cast(
                        Authentication::class.java.cast(p)
                            .principal
                    )
                }
            )

}