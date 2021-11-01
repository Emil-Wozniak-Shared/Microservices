package pl.emil.customers.web

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pl.emil.customers.model.Customer
import pl.emil.customers.service.CustomerService

@RestController
class CustomerController(private val service: CustomerService) {

    @GetMapping("/customers", produces = [APPLICATION_JSON_VALUE])
    fun getAll(): ResponseEntity<List<Customer?>> =
        ok().contentType(APPLICATION_JSON).body(service.findAll())

    @GetMapping("/customers/{id}", produces = [APPLICATION_JSON_VALUE])
    fun getById(@PathVariable("id") id: Long): ResponseEntity<Customer> =
        service.findById(id)
            .map {
                ok().contentType(APPLICATION_JSON).body(it)
            }
            .orElse(
                badRequest().contentType(APPLICATION_JSON).build()
            )
}
