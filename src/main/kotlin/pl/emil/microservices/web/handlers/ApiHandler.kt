package pl.emil.microservices.web.handlers

import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_XML
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.SmartValidator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import pl.emil.microservices.web.exception.RequestNonValidException
import reactor.core.publisher.Mono
import java.util.*

interface ApiHandler<T> {
    fun all(request: ServerRequest): Mono<ServerResponse>
    fun getOne(request: ServerRequest): Mono<ServerResponse>
    fun create(request: ServerRequest): Mono<ServerResponse>
    fun update(request: ServerRequest): Mono<ServerResponse>
    fun delete(request: ServerRequest): Mono<ServerResponse>
}

fun ServerRequest.mediaType(): MediaType {
    val accept = this.headers().accept()
    return when {
        APPLICATION_XML in accept -> APPLICATION_XML
        APPLICATION_JSON in accept -> APPLICATION_JSON
        else -> APPLICATION_JSON
    }
}

fun String.toUUID(): UUID = UUID.fromString(this)

fun ServerRequest.id(): UUID = this.pathVariable("id").toUUID()

inline fun <reified T> ServerRequest.validateBody(
    validator: SmartValidator,
    vararg nonNullFields: Any
): Mono<T> =
    this
        .bodyToMono(T::class.java)
        .doOnNext { body ->
            val errors = BeanPropertyBindingResult(body, T::class.java.name)
            validator.validate(body as Any, errors, *nonNullFields)
            if (errors.allErrors.isEmpty()) Mono.just(body)
            else throw RequestNonValidException(message = errors.allErrors.toString())
        }
