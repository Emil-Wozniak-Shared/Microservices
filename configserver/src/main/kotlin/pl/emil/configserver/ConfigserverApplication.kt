package pl.emil.configserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer
import pl.emil.configserver.config.thenLog

@EnableConfigServer
@SpringBootApplication
class ConfigserverApplication

fun main(args: Array<String>) {
    runApplication<ConfigserverApplication>(*args).thenLog()
}
