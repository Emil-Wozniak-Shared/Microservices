package pl.emil.users.config

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.MediaType.APPLICATION_XML
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.SmartValidator
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import java.util.*
//
//interface ApiHandler<T> {
//    fun getAll(request: ServerRequest): Mono<ServerResponse>
//    fun getOne(request: ServerRequest): Mono<ServerResponse>
//    fun create(request: ServerRequest): Mono<ServerResponse>
//    fun update(request: ServerRequest): Mono<ServerResponse>
//    fun delete(request: ServerRequest): Mono<ServerResponse>
//}
//
//fun ServerResponse.BodyBuilder.mediaType(request: ServerRequest): ServerResponse.BodyBuilder =
//    HttpHeaders()
//        .apply { add(ACCEPT, request.headers().accept().toString()) }
//        .let { headers ->
//            if (request.headers().accept().contains(APPLICATION_XML)) headers { it.addAll(headers) }.xml()
//            else headers { it.addAll(headers) }.json()
//        }
//
//fun String.toUUID(): UUID = UUID.fromString(this)
//
//fun ServerRequest.id(): UUID = this.pathVariable("id").toUUID()
//
//inline fun <reified T> ServerRequest.validateBody(validator: SmartValidator, vararg nonNullFields: Any): Mono<T> =
//    this.bodyToMono(T::class.java)
//        .doOnNext { body ->
//            BeanPropertyBindingResult(body, T::class.java.name).let { errors ->
//                validator.validate(body as Any, errors, *nonNullFields)
//                if (errors.allErrors.isEmpty()) Mono.just(body)
//                else throw RequestNonValidException(errors.toString())
//            }
//        }
//
//fun Mono<ServerResponse>.onErrorResponse(status: HttpStatus = BAD_REQUEST): Mono<ServerResponse> =
//    this.onErrorResume {
//        ServerResponse
//            .status(status)
//            .body(Mono.just(ExceptionResponse(it.message, it.cause.toString())))
//    }
