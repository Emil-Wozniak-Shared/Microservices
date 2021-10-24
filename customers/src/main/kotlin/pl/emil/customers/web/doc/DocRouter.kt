package pl.emil.customers.web.doc

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.emil.customers.handler.UserHandler
import pl.emil.customers.repo.UserRepository
import pl.emil.customers.utils.doc.docRouter

@Configuration
class DocRouter(private val userHandler: UserHandler) {
    @Bean
    fun docUserRoutes() =
        docRouter(UserRepository::class.java) {
            "/api/user".nest {
                GET("/", "getAllUsers", userHandler::getAll)
                GET("/{id}", "getUserById", userHandler::getUser)
                POST("/post", "saveUser", userHandler::postUser)
                PUT("/put/{id}", "putUser", userHandler::putUser)
                DELETE("/delete/{id}", "deleteUser", userHandler::deleteUser)
            }
        }
}

