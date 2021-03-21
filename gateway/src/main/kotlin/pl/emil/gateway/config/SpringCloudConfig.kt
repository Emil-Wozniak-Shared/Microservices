package pl.emil.gateway.config

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringCloudConfig(
    private val cookieFilter: AuthorizationCookieFilter
) {

    @Bean
    fun additionalRouteLocator(builder: RouteLocatorBuilder) = builder.routes {
        route(id = "users", uri = "lb://users") {
            path("/users/api/login")
            path("/users/api/users")
//                .filters {
//                it.filter(cookieFilter)
//            }
        }
        route(id = "posts", uri = "lb://posts") {
            host("localhost") and path("/posts/**")
            filters {
                cookieFilter
            }
        }
    }
}