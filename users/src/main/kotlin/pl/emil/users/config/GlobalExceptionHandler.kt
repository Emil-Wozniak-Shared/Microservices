package pl.emil.users.config

import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import pl.emil.common.config.StandardErrorWebExceptionHandler

@Component
@Order(-2)
class GlobalErrorWebExceptionHandler(
    attributes: GlobalErrorAttributes,
    context: ApplicationContext,
    codecConfigurer: ServerCodecConfigurer,
): StandardErrorWebExceptionHandler(attributes, context, codecConfigurer)
