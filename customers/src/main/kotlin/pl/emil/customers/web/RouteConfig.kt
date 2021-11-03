package pl.emil.customers.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.router
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import pl.emil.customers.handler.CustomerHandler
import pl.emil.customers.model.Customer
import reactor.core.publisher.Flux
import java.util.concurrent.atomic.AtomicInteger

@Controller
class RouteConfig(private val mapper: ObjectMapper) {

    @Throws(Exception::class)
    private fun from(customer: Customer): String? = mapper.writeValueAsString(customer)

    @Bean
    fun customerWsh(customers: Flux<Customer>): WebSocketHandler =
        WebSocketHandler { session ->
            session.send {
                customers.map { customer ->
                    println(customer.toString())
                    session.textMessage(from(customer) ?: "<null>")
                }
            }
        }

    @Bean
    fun simpleUrlHandlerMapping(customerWsh: WebSocketHandler) =
        SimpleUrlHandlerMapping(hashMapOf("/ws/customer" to customerWsh))

    val countErrors = hashMapOf<String, AtomicInteger>()

    @Bean
    fun routes(customers: Flux<Customer>, customerHandler: CustomerHandler) =
        router {
            "error".nest {
                GET("{id}") {
                    val id = it.pathVariable("id")
                    val result = countResponse(id)?.get() ?: 0
                    if (result < 5) {
                        status(SERVICE_UNAVAILABLE).build()
                    } else {
                        ok().bodyValue(mapOf(
                            "message" to "good job",
                            id to ", you did it on try #${countErrors}"
                        ))
                    }
                }
            }
            "api".nest {
                "customers".nest {
                    GET("", customerHandler::getAll)
                }
            }
        }

    private fun countResponse(id: String) =
        countErrors.compute(id) { _, atomInt ->
            if (null == atomInt) AtomicInteger(0)
            else {
                atomInt.incrementAndGet()
                atomInt
            }
        }
}

