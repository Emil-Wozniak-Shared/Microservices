package pl.emil.customers.service

import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import pl.emil.customers.model.Customer
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Duration.ofSeconds

@Service
class CustomerService(
    private val env: Environment
) {

    private val names = "${env.getProperty("config.server.users")},".split(",")

    @Bean
    fun customers() = customers.publish().autoConnect()

    private val customers = Flux
        .fromStream {
            names.mapIndexed { index, name ->
                Customer(index.toLong(), name)
            }.stream()
        }
        .delayElements(ofSeconds(3))
}
