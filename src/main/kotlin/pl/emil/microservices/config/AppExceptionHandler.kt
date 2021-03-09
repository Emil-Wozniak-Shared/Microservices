package pl.emil.microservices.config

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.handler.ResponseStatusExceptionHandler
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.badRequest

@ControllerAdvice
class AppExceptionHandler : ResponseStatusExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleAnyError(e: Exception, request: WebRequest): ServerResponse =
        badRequest().body(e.stackTrace)
}