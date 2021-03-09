package pl.emil.microservices.web.handlers

import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_XML
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
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

fun ServerRequest.id() = this.pathVariable("id").toUUID()
