package pl.emil.users.web

import org.springframework.core.env.Environment
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
    private val signer: JwtSigner,
    private val env: Environment,
) : ApiHandler<User> {

    private val credentials = arrayOf("email", "password")

    fun token(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(Login::class.java)
            .flatMap { login ->
                service.getToken(login.username).flatMap { token ->
                    ok().bodyValue(token)
                }
            }
            .onErrorResponse()

    override fun getAll(request: ServerRequest): Mono<ServerResponse> =
        request.headers().contentType().let { contentType ->
            if (contentType.isPresent && contentType.get() == APPLICATION_XML)
                ok().mediaType(request).body(service.getAllAsXML())
            else ok().body(service.getAll())
        }

    override fun getOne(request: ServerRequest): Mono<ServerResponse> =
        request.id().let { id ->
            service
                .getOne(id)
                .flatMap { user ->
                    user?.let {
                        ok().mediaType(request).bodyValue(it)
                    } ?: badRequest().bodyValue("User with id: $id doent't exist")
                }
                .onErrorResponse()
        }

    override fun create(request: ServerRequest): Mono<ServerResponse> =
        request
            .validateBody<User>(validator, "email", "firstName", "lastName")
            .doOnNext { user -> service.create(user).subscribe() }
            .flatMap { user ->
                if (user == null) error(UserCreateException("That user can't be created"))
                else created(URI.create("/users/${user.id}")).bodyValue(user)
            }

    override fun update(request: ServerRequest): Mono<ServerResponse> =
        badRequest().mediaType(request).build()

    override fun delete(request: ServerRequest): Mono<ServerResponse> =
        badRequest().mediaType(request).build()

    @Deprecated(
        "method creates not valid token",
        ReplaceWith("pl.emil.users.web.UserHandler.token")
    )
    fun login(request: ServerRequest): Mono<ServerResponse> =
        request
            .validateBody<UserCredentials>(validator, *credentials)
            .flatMap { service.findByUsername(it.email) }
            .filter { it != null }
            .map {
                it?.let {
                    fromClientResponse("X-Auth", signer.createJwt(it.username))
                        .maxAge(env.getProperty("token.expiration_time")?.toLong() ?: 7200)
                        .httpOnly(true)
                        .path("/")
                        .secure(false) // should be true in production
                        .build()
                }
            }
            .flatMap { token ->
                noContent()
                    .headers { headers -> headers.add("Set-Cookie", token.toString()) }
                    .build()

            }
            .switchIfEmpty(status(UNAUTHORIZED).build())
}


