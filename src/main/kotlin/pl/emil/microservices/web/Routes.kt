package pl.emil.microservices.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_XML
import org.springframework.web.servlet.function.router
import pl.emil.microservices.model.user.User
import pl.emil.microservices.web.handlers.ApiHandler
import pl.emil.microservices.web.handlers.CommentHandler
import pl.emil.microservices.web.handlers.PostHandler

@Configuration
class Routes {
    @Bean(value = ["allRoutes"])
    fun routes(
        users: ApiHandler<User>,
        posts: PostHandler,
        comments: CommentHandler,
    ) = router {
        "api".nest {
            "/users".nest {
                accept(APPLICATION_JSON, APPLICATION_XML).nest {
                    GET("", users::all)
                    GET("/{id}", users::getOne)
                    contentType(APPLICATION_JSON).nest {
                        POST("", users::create)
                        PUT("/{id}", users::update)
                    }
                    DELETE("/{id}", users::delete)
                }
            }
            "/posts".nest {
                accept(APPLICATION_JSON, APPLICATION_XML).nest {
                    "".nest {
                        GET("", posts::all)
                        POST("", posts::create)
                    }
                    "{id}".nest {
                        GET("", posts::getOne)
                        PUT("", posts::update)
                        DELETE("", posts::delete)
                    }
                    "comments".nest {
                        GET("", comments::getByPostId)
                        POST("", comments::create)
                    }
                }
            }
        }
    }
}