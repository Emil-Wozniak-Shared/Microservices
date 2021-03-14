package pl.emil.users.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router
import pl.emil.users.config.ApiHandler
import pl.emil.users.model.User

@Configuration
class Routes {
    @Bean(value = ["allRoutes"])
    fun routes(
        users: ApiHandler<User>,
    ) = router {
        "api".nest {
            "/users".nest {

                "".nest {
                    GET("", users::all)
                    POST("", users::create)
                }
                "{id}".nest {
                    GET("", users::getOne)
                    PUT("", users::update)
                    DELETE("", users::delete)
                }

            }
        }
    }
}