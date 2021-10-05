package pl.emil.common.config

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.error.ErrorAttributeOptions.Include.*
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.status
import pl.emil.common.config.exception.ExceptionResponse
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

open class CommonGlobalErrorAttributes(
    open val status: HttpStatus = BAD_REQUEST,
    open val message: String = "",
) : DefaultErrorAttributes() {

    private val all = listOf(MESSAGE, BINDING_ERRORS, EXCEPTION, STACK_TRACE)
    private val options = ErrorAttributeOptions.of(all)

    override fun getErrorAttributes(
        request: ServerRequest,
        options: ErrorAttributeOptions,
    ): MutableMap<String, Any> = super.getErrorAttributes(request, options).apply {
        this["status"] = status
        this["message"] = message
    }

    fun getErrorResponse(request: ServerRequest): Mono<ServerResponse> =
        with(this.getErrorAttributes(request, options)) {
            val status = get("status") as HttpStatus
            val message = get("trace") as String
            val requestId = get("requestId") as String
            val cause = get("exception") as String
            val timestamp = (get("timestamp") as Date).toLocalDate()
            val response = ExceptionResponse(message, cause, requestId, timestamp)
            status(status).contentType(APPLICATION_JSON).body(fromValue(response))
        }
}

fun Date.toLocalDate(): LocalDateTime = this.toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()
