package pl.emil.gateway.config

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.emil.gateway.factory.HeaderGatewayFilterFactory
import pl.emil.gateway.factory.HeaderGatewayFilterFactory.HeaderConfig

@Configuration
class SpringCloudConfig {

    @Bean
    fun additionalRouteLocator(
        builder: RouteLocatorBuilder,
        headerFactory: HeaderGatewayFilterFactory
    ) = builder.routes {
        route(id = "users", uri = "lb://users") {
            host("localhost") and path("/users/**")
            filters {
                rewritePath("/users(?<segment>/?.*)", "$\\{segment}")
                    .filter(headerFactory.apply(HeaderConfig("api")))
            }
            build()
        }
        route(id = "posts", uri = "lb://posts") {
            host("localhost") and path("/posts/**")
        }
        route(id = "service_route_java_config", uri = "http://localhost:8081") {
            path("/service/**")
                .and()
                .filters {
                    rewritePath("/users(?<segment>/?.*)", "$\\{segment}")
                        .filter(headerFactory.apply(HeaderConfig("api")))
                }

        }
    }
}
