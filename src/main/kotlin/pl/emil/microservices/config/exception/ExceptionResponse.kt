package pl.emil.microservices.config.exception

import java.time.Instant
import java.time.Instant.now

data class ExceptionResponse(
    val message: String?,
    val cause: Throwable,
    val timestamp: Instant = now()
)