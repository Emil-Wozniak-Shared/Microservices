package pl.emil.customers.service

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import pl.emil.customers.model.Customer
import pl.emil.customers.repo.CustomerRepo
import reactor.core.publisher.Flux
import java.time.Duration.ofSeconds
import java.util.*

@Service
@Tag(name = "Customers")
class CustomerService(private val repo: CustomerRepo) {

    @Bean
    fun customers() = customers.publish().autoConnect()

    @Operation(description = "get all the customers")
    fun getAll(): Flux<Customer> = customers

    fun findAll(): List<Customer> = repo.findAll()

    fun findById(id: Long): Optional<Customer> = repo.findById(id)

    private val customers = Flux
        .fromStream { repo.findAll().stream() }
        .delayElements(ofSeconds(3))
}
