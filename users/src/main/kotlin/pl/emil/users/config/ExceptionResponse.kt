package pl.emil.users.config

import java.time.LocalDateTime

data class ExceptionResponse(
    val message: String?,
    val cause: String? = null,
    val requestId: String? = null,
    val timestamp: LocalDateTime? = LocalDateTime.now()
)