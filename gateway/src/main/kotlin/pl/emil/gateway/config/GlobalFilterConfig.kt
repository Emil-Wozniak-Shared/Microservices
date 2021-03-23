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

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    @Order(3)
    fun globally1(): GlobalFilter = GlobalFilter { exchange, chain ->
        logger.info("GlobalFilterConfig 1 entering")
        chain.filter(exchange).then(Mono.fromRunnable {
            logger.info("GlobalFilterConfig 1 exiting")
        })
    }

    @Bean
    @Order(4)
    fun globally2(): GlobalFilter = GlobalFilter { exchange, chain ->
        logger.info("GlobalFilterConfig 2 entering")
        chain.filter(exchange).then(Mono.fromRunnable {
            logger.info("GlobalFilterConfig 2 exiting")
        })
    }

    @Bean
    @Order(5)
    fun globally3(): GlobalFilter = GlobalFilter { exchange, chain ->
        logger.info("GlobalFilterConfig 3 entering")
        chain.filter(exchange).then(Mono.fromRunnable {
            logger.info("GlobalFilterConfig 3 exiting")
        })
    }
}