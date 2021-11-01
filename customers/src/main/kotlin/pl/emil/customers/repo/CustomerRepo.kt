package pl.emil.customers.repo

import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import pl.emil.customers.model.Customer
import java.util.*

@Component
class CustomerRepo(private val env: Environment) {
    private final val names = "${env.getProperty("config.server.users")},".split(",")
    private val store = names.mapIndexed { index, name ->
        Customer(index.toLong(), name)
    }

    fun findAll() = this.store

    fun findById(id: Long): Optional<Customer> = Optional.of(store.first { it.id == id })
}
