package pl.emil.microservices.config

import org.springframework.boot.web.error.ErrorAttributeOptions

import reactor.core.publisher.Mono

import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.*


@Component
@Order(-2)
class GlobalErrorWebExceptionHandler : AbstractErrorWebExceptionHandler() {
    // constructors
    override fun getRoutingFunction(errorAttributes: org.springframework.boot.web.reactive.error.ErrorAttributes?): org.springframework.web.reactive.function.server.RouterFunction? {
        return RouterFunctions.route(
            RequestPredicates.all(),  {
                    request: ServerRequest -> renderErrorResponse(request).b
            })
    }

    private fun renderErrorResponse(
        request: ServerRequest
    ): ServerResponse {
        val errorPropertiesMap: Map<String, Any> = getErrorAttributes(
            request,
            ErrorAttributeOptions.defaults()
        )
        return ServerResponse.status(BAD_REQUEST)
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(errorPropertiesMap))
    }
}
