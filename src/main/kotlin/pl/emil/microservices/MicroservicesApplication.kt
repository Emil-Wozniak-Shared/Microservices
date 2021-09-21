package pl.emil.microservices

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.context.annotation.Bean

@EnableEurekaServer
@SpringBootApplication
class MicroservicesApplication

fun main(args: Array<String>) {
    runApplication<MicroservicesApplication>(*args)
}
