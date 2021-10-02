package pl.emil.gateway.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import reactor.core.publisher.Mono

@Configuration
class GlobalFilterConfig {
    private val name: String = "GlobalFilterConfig.class"
    private val log: Logger = LoggerFactory.getLogger(name)

    private fun entrance(nr: Int, enter: Boolean = true) {
        log.info(" ${if (enter) ">>" else "<<"} $name Order execute=$nr")
    }

    @Bean
    @Order(3)
    fun globally1(): GlobalFilter = GlobalFilter { exchange, chain ->
        entrance(1)
        chain.filter(exchange).then(Mono.fromRunnable {
            entrance(1, false)
        })
    }

    @Bean
    @Order(4)
    fun globally2(): GlobalFilter = GlobalFilter { exchange, chain ->
        entrance(2)
        chain.filter(exchange).then(Mono.fromRunnable {
            entrance(2, false)
        })
    }
}
