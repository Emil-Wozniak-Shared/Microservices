package pl.emil.users.web

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP
import org.springdoc.core.annotations.RouterOperation
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import pl.emil.contract.api.UserApi

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

    @Bean("apiRoutes")
    @RouterOperation(beanClass = UserApi::class, beanMethod = "getAllUsers")
    fun getAllEmployeesRoute(): RouterFunction<ServerResponse> =
        route(GET("/getAllUsers")) {
            ok().bodyValue("ok")
        }

//    @Bean
//    fun customOpenAPI(): OpenAPI? {
//        return OpenAPI()
//            .components(
//                Components().addSecuritySchemes(
//                    "basicScheme",
//                    SecurityScheme().type(HTTP).scheme("basic")
//                )
//            )
//            .info(
//                Info()
//                    .title("Tweet API")
//                    .version("v0")
//                    .license(License().name("Apache 2.0").url("http://springdoc.org"))
//            )
//    }
}
