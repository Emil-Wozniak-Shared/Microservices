package pl.emil.microservices.config

import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.APPLICATION_XML_VALUE
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.body
import org.springframework.web.server.handler.ResponseStatusExceptionHandler
import reactor.core.publisher.Mono

@ControllerAdvice
class AppExceptionHandler : ResponseStatusExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleAnyError(): Mono<ServerResponse> = badRequest()
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .header(ACCEPT, APPLICATION_XML_VALUE)
        .body(Mono.just("Bad request"))
}