package pl.emil.microservices.config.exception

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.error.ErrorAttributeOptions.Include.*
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
data class GlobalErrorAttributes(
    val status: HttpStatus = BAD_REQUEST,
    val message: String = ""
) : DefaultErrorAttributes() {

    private val options = ErrorAttributeOptions.of(MESSAGE, BINDING_ERRORS, EXCEPTION, STACK_TRACE)

    override fun getErrorAttributes(
        request: ServerRequest,
        options: ErrorAttributeOptions
    ): MutableMap<String, Any> = super.getErrorAttributes(request, options).apply {
        this["status"] = status
        this["message"] = message
    }

    fun asErrorResponse(request: ServerRequest): ExceptionResponse =
        with(this.getErrorAttributes(request, options)) {
            val status = get("status") as HttpStatus
            val message = get("trace") as String
            val requestId = get("requestId") as String
            val cause = get("exception") as String
            val timestamp = (get("timestamp") as Date).toLocalDate()
            ExceptionResponse(message, cause, requestId, timestamp, status)
        }
}

fun Date.toLocalDate(): LocalDateTime? = this.toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()