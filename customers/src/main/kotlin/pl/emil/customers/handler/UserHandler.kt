package pl.emil.customers.handler

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import pl.emil.customers.model.User
import pl.emil.customers.repo.UserRepository
import reactor.core.publisher.Mono

@Component
class UserHandler(private val customerRepository: UserRepository) {
    /**
     * GET ALL Users
     */
    fun getAll(request: ServerRequest): Mono<ServerResponse> =
        ok().contentType(APPLICATION_JSON).body(customerRepository.getAll())

    /**
     * GET a User by ID
     */
    fun getUser(request: ServerRequest): Mono<ServerResponse> =
        customerRepository.getUserById(request.pathVariable("id").toLong())
            .flatMap {
                it?.let { ok().contentType(APPLICATION_JSON).bodyValue(it) }
            }
            .switchIfEmpty(notFound().build())

    /**
     * POST a User
     */
    fun postUser(request: ServerRequest): Mono<ServerResponse> {
        val customer: Mono<User> = request.bodyToMono(User::class.java)
        return ok().build(customerRepository.saveUser(customer))
    }

    /**
     * PUT a User
     */
    fun putUser(request: ServerRequest): Mono<ServerResponse> = request
        .run {
            customerRepository
                .putUser(
                    id = pathVariable("id").toLong(),
                    user = bodyToMono()
                )
                .flatMap {
                    it?.let { ok().contentType(APPLICATION_JSON).bodyValue(it) } ?: badRequest().build()
                }
        }

    /**
     * DELETE a User
     */
    fun deleteUser(request: ServerRequest): Mono<ServerResponse> =
        customerRepository
            .deleteUser(request.pathVariable("id").toLong())
            .flatMap { ok().contentType(TEXT_PLAIN).bodyValue(it) }

}
