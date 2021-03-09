package pl.emil.microservices

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class MicroservicesApplication

fun main(args: Array<String>) {
    runApplication<MicroservicesApplication>(*args)

}