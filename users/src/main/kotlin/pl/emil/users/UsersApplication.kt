package pl.emil.users

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.context.config.annotation.RefreshScope

@RefreshScope
@SpringBootApplication
class UsersApplication

fun main(args: Array<String>) {
    runApplication<UsersApplication>(*args)
}
