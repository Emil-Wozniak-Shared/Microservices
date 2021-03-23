package pl.emil.gateway.config

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.emil.gateway.config.HeaderGatewayFilterFactory.HeaderConfig
import pl.emil.gateway.factory.LoggingGatewayFilterFactory
import pl.emil.gateway.factory.LoggingGatewayFilterFactory.Config

@Configuration
class SpringCloudConfig {

    @Bean
    fun additionalRouteLocator(
        builder: RouteLocatorBuilder,
        loggingFactory: LoggingGatewayFilterFactory,
        headerFactory: HeaderGatewayFilterFactory
    ) = builder.routes {
        route(id = "users", uri = "lb://users") {
            path("/users/**")
                .and()
                .filters {
                    rewritePath("/users(?<segment>/?.*)", "$\\{segment}")
                        .filter(loggingFactory.apply(Config("Users entered")))
                        .filter(headerFactory.apply(HeaderConfig("api")))
                }
        }
        route(id = "posts", uri = "lb://posts") {
            host("localhost") and path("/posts/**")
        }
        route(id = "service_route_java_config") {
            path("/service/**")
                .filters { f ->
                    f.rewritePath("/service(?<segment>/?.*)", "$\\{segment}")
                        .filter(loggingFactory.apply(Config("My Custom Message", true, true)))
                }
                .uri("http://localhost:8081")
        }
    }
}