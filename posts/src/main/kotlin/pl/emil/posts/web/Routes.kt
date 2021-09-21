package pl.emil.posts.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_XML
import org.springframework.web.reactive.function.server.router
import pl.emil.posts.model.Comment
import pl.emil.posts.model.Post
import pl.emil.posts.web.handlers.ApiHandler

@Configuration
class Routes {
    @Bean(value = ["allRoutes"])
    fun routes(posts: ApiHandler<Post>, comments: ApiHandler<Comment>) =
        router {
            "api".nest {
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
                            GET("", comments::getOne)
                            POST("", comments::create)
                        }
                    }
                }
            }
        }
}
