package pl.emil.microservices.config.exception

import org.springframework.boot.autoconfigure.web.WebProperties.Resources
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates.all
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
@Order(-2)
class GlobalErrorWebExceptionHandler(
    attributes: GlobalErrorAttributes,
    context: ApplicationContext,
    codecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(attributes, Resources(), context) {

    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions):
            MutableMap<String, Any> = super.getErrorAttributes(request, options)

    override fun getRoutingFunction(attributes: ErrorAttributes):
            RouterFunction<ServerResponse> = route(all())
    { renderErrorResponse(it, attributes as GlobalErrorAttributes) }

    private fun renderErrorResponse(
        request: ServerRequest,
        attributes: GlobalErrorAttributes
    ): Mono<ServerResponse> = attributes.getErrorResponse(request)

    init {
        super.setMessageWriters(codecConfigurer.writers)
        super.setMessageReaders(codecConfigurer.readers)
    }
}
