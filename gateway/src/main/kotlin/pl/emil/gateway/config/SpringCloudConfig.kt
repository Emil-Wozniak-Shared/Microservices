package pl.emil.gateway.config

import org.springframework.cloud.gateway.filter.OrderedGatewayFilter
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.emil.gateway.factory.HeaderGatewayFilterFactory
import pl.emil.gateway.factory.HeaderGatewayFilterFactory.HeaderConfig
import reactor.kotlin.core.publisher.toMono

@Configuration
class SpringCloudConfig {

    private fun String.toRouteUri() = "lb://$this"
    private fun String.toSegmentRegex() = "/$this(?<segment>/?.*)"
    private fun String.withSubPaths() = "/$this/**"

    @Bean
    fun additionalRouteLocator(
        builder: RouteLocatorBuilder,
        headerFactory: HeaderGatewayFilterFactory
    ) = builder.routes {
        "users".let { path ->
            route(id = path, uri = path.toRouteUri()) {
                host(LOCALHOST) and path(path.withSubPaths())
                filters {
                    rewritePath(path.toSegmentRegex(), REPLACE_SEGMENT)
                        .filter(headerFactory.apply(HeaderConfig(API)))
                }
                build()
            }
        }
        "posts".let { path ->
            route(id = path, uri = path.toRouteUri()) {
                host(LOCALHOST) and path(path.withSubPaths())
            }
        }
        route(id = "test-route", uri = "customers".toRouteUri()) {
            asyncPredicate {
                it.request.uri.path.contains("customers").toMono()
            }
        }
        route(id = "service_route_java_config", uri = "http://localhost:8081") {
            path("/service/**")
                .and()
                .filters {
                    rewritePath("users".toSegmentRegex(), REPLACE_SEGMENT)
                        .filter(headerFactory.apply(HeaderConfig(API)))
                }
        }
    }
}
