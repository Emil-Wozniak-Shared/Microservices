package pl.emil.common.config

import org.springframework.boot.autoconfigure.web.WebProperties.Resources
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.function.server.RequestPredicates.all
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

open class StandardErrorWebExceptionHandler (
    attributes: CommonGlobalErrorAttributes,
    context: ApplicationContext,
    codecConfigurer: ServerCodecConfigurer,
    ) : AbstractErrorWebExceptionHandler(attributes, Resources(), context) {

        override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions):
                MutableMap<String, Any> = super.getErrorAttributes(request, options)

        override fun getRoutingFunction(attributes: ErrorAttributes): RouterFunction<ServerResponse> = route(all()) {
            renderErrorResponse(it, attributes as CommonGlobalErrorAttributes)
        }

        private fun renderErrorResponse(request: ServerRequest, attributes: CommonGlobalErrorAttributes ): Mono<ServerResponse> =
            attributes.getErrorResponse(request)

        init {
            super.setMessageWriters(codecConfigurer.writers)
            super.setMessageReaders(codecConfigurer.readers)
        }
    }
