package pl.emil.users.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class RouteConfig(@Value("\${token.expiration_time}") private val expiration: String) {

    @Bean
    fun routes(users: UserHandler) =
        router {
            "/yml".nest {
                GET("") {
                    ok().bodyValue(expiration)
                }
            }
            "/oauth/token".nest {
                POST(users::token)
            }
            "/api".nest {
                "/login".nest {
                    POST(users::login)
                }
                "/users".nest {
                    "{id}".nest {
                        GET(users::getOne)
                        PUT(users::update)
                        DELETE(users::delete)
                    }
                    "".nest {
                        GET(users::getAll)
                        POST(users::create)
                    }
                }
            }
        }
}
