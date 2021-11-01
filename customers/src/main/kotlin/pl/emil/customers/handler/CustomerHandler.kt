package pl.emil.customers.handler

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import pl.emil.customers.model.Customer
import pl.emil.customers.service.CustomerService

@Component
class CustomerHandler(private val customerService: CustomerService) {
    fun getAll(request: ServerRequest) =
        ok().contentType(APPLICATION_JSON).body<Customer>(customerService.getAll())
}

