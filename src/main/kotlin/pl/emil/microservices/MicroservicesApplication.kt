package pl.emil.microservices

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.context.ConfigurableApplicationContext
import pl.emil.microservices.config.StartLogger
import pl.emil.microservices.config.thenLog

@EnableEurekaServer
@SpringBootApplication
class MicroservicesApplication

fun main(args: Array<String>) {
    runApplication<MicroservicesApplication>(*args).thenLog()
}

