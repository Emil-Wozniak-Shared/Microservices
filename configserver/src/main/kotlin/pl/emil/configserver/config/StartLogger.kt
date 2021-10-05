package pl.emil.configserver.config

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.net.InetAddress.getLocalHost

@Component
class StartLogger(private val env: Environment) {
    fun write() {
        println(
            """
            ______________________________________________________
            | Application name: =>  ${env.getProperty("spring.application.name")}
            | Zone:             =>  ${env.getProperty("eureka.client.service_url.defaultZone")}
            | Address:          =>  ${getLocalHost().hostName}@${getLocalHost().hostAddress}:${env.getProperty("server.port")}
            | Config address:   =>  ${env.getProperty("spring.cloud.config.server.git.uri")}
            | Config username:  =>  ${env.getProperty("spring.cloud.config.server.git.username")}
            ______________________________________________________
        """.trimIndent()
        )
    }
}

fun ConfigurableApplicationContext.thenLog() = (this.getBean("startLogger") as StartLogger).write()

