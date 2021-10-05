package pl.emil.users.config

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.stereotype.Component
import pl.emil.common.config.CommonGlobalErrorAttributes

@Component
data class GlobalErrorAttributes(
    override val status: HttpStatus = BAD_REQUEST,
    override val message: String = ""
) : CommonGlobalErrorAttributes(status, message)
