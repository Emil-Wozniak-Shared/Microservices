package pl.emil.pdf.config

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.net.InetAddress

@Component
class StartLogger(private val env: Environment) {
    fun write() {
        println("""
            ______________________________________________________
            | Application name: =>  ${env.getProperty("spring.application.name")}
            | Zone:             =>  ${env.getProperty("eureka.client.service-url.defaultZone")}
            | Address:          =>  ${InetAddress.getLocalHost().hostName}@${InetAddress.getLocalHost().hostAddress}:${env.getProperty("server.port")}
            ______________________________________________________
        """.trimIndent())
    }
}

fun ConfigurableApplicationContext.thenLog() = (this.getBean("startLogger") as StartLogger).write()

