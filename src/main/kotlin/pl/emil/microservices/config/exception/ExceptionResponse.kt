package pl.emil.microservices.config.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ExceptionResponse(
    val message: String?,
    val cause: String? = null,
    val requestId: String? = null,
    val timestamp: LocalDateTime? = LocalDateTime.now(),
    val status: HttpStatus = HttpStatus.BAD_REQUEST
)