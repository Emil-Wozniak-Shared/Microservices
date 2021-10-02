package pl.emil.microservices.config

import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.net.InetAddress.getLocalHost

@Component
class StartLogger(private val env: Environment) {
    fun write() {
        getLocalHost().hostAddress
        getLocalHost().hostName
        println("""
            ______________________________________________________
            | Application name: =>  ${env.getProperty("spring.application.name")}
            | Zone:             =>  ${env.getProperty("eureka.client.service_url.defaultZone")}
            | Address:          =>  ${getLocalHost().hostName}@${getLocalHost().hostAddress}:${env.getProperty("server.port")}
            ______________________________________________________
        """.trimIndent())
    }
}
