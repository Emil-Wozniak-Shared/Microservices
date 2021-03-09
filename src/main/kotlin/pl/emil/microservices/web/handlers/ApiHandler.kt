package pl.emil.microservices.web.handlers

import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_XML
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.util.*

interface ApiHandler<T> {
    fun all(request: ServerRequest): ServerResponse
    fun getOne(request: ServerRequest): ServerResponse
    fun create(request: ServerRequest): ServerResponse
    fun update(request: ServerRequest): ServerResponse
    fun delete(request: ServerRequest): ServerResponse
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
