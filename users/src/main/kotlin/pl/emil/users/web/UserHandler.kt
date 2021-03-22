package pl.emil.users.web

import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_XML
import org.springframework.http.ResponseCookie.fromClientResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import pl.emil.users.config.*
import pl.emil.users.config.exception.UserCreateException
import pl.emil.users.model.User
import pl.emil.users.model.UserCredentials
import pl.emil.users.security.token.JwtSigner
import pl.emil.users.service.UserService
import reactor.core.publisher.Mono
import java.net.URI

data class Login(val username: String)
data class Token(val token: String, val expiresIn: Int)

@Service
class UserHandler(
    private val service: UserService,
    private val validator: RequestValidator,
    private val signer: JwtSigner
) : ApiHandler<User> {

    private val credentials = arrayOf("email", "password")

    fun token(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(Login::class.java)
            .flatMap { login ->
                ok().bodyValue(Token(signer.createJwt(login.username), 7200))
            }

    fun login(request: ServerRequest): Mono<ServerResponse> =
        request
            .validateBody<UserCredentials>(validator, *credentials)
            .flatMap { service.findByUsername(it.email) }
            .filter { it != null }
            .map {
                fromClientResponse("X-Auth", signer.createJwt(it!!.username))
                    .maxAge(3600)
                    .httpOnly(true)
                    .path("/")
                    .secure(false) // should be true in production
                    .build()
            }
            .flatMap { token ->
                noContent()
                    .headers { headers ->
                        headers.add("Set-Cookie", token.toString())
                    }
                    .build()

            }
            .switchIfEmpty(status(UNAUTHORIZED).build())

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
                service.create(user).subscribe()
            }
            .flatMap {
                if (it == null) error(UserCreateException("That user can't be created"))
                else created(URI.create("/users/${it.id}")).build()
            }

    override fun update(request: ServerRequest): Mono<ServerResponse> =
        badRequest().mediaType(request).build()

    override fun delete(request: ServerRequest): Mono<ServerResponse> =
        badRequest().mediaType(request).build()

}