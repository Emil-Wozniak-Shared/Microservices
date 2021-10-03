package pl.emil.customers.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.TEXT_EVENT_STREAM
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import pl.emil.customers.model.Customer
import reactor.core.publisher.Flux

@Configuration
class RouteConfig(private val mapper: ObjectMapper) {

    @Throws(Exception::class)
    private fun from(customer: Customer): String? = mapper.writeValueAsString(customer)

    @Bean
    fun webSocketAdapter(customers: Flux<Customer>): WebSocketHandler =
        WebSocketHandler { session ->
            session.send {
                customers.map { customer ->
                    session.textMessage(from(customer) ?: "<null>")
                }
            }
        }

    @Bean
    fun simpleUrlHandlerMapping(customerWsh: WebSocketHandler) =
        SimpleUrlHandlerMapping(hashMapOf("/ws/customer" to customerWsh))

    @Bean
    fun routes(customers: Flux<Customer>) = router {
        "api".nest {
            "customers".nest {
                GET("") {
                    ok().contentType(TEXT_EVENT_STREAM).body(customers)
                }
            }
        }
    }
}

