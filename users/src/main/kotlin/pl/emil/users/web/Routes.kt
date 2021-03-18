package pl.emil.users.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class Routes {
    @Bean(value = ["allRoutes"])
    fun routes(
        users: UserHandler
    ) = router {
        "/api".nest {
            "/login".nest {
                POST(users::login)
            }
            "/signup".nest {
                POST(users::signUp)
            }
            "/users".nest {
                "".nest {
                    GET(users::all)
                    POST(users::create)
                }
                "{id}".nest {
                    GET(users::getOne)
                    PUT(users::update)
                    DELETE(users::delete)
                }
            }
        }
    }
}