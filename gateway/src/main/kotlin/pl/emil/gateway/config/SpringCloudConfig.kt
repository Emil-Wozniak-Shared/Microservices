package pl.emil.gateway.config

import org.springframework.cloud.gateway.route.builder.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.emil.gateway.factory.HeaderGatewayFilterFactory
import pl.emil.gateway.factory.HeaderGatewayFilterFactory.HeaderConfig
import reactor.kotlin.core.publisher.toMono

@Configuration
class SpringCloudConfig(private val headerFactory: HeaderGatewayFilterFactory) {

    @Bean
    fun additionalRouteLocator(builder: RouteLocatorBuilder) =
        builder.routes {
            endpoint("users") {
            }
            endpoint("posts") {
            }
            endpoint("customers") {
                asyncPredicate {
                    it.request.uri.path.contains("customers").toMono()
                }
            }
            endpoint(
                "http://localhost:8182",
                "service_route_java_config",
                loadBalance = false,
                subPaths = false
            ) {
            }
        }

    private fun RouteLocatorDsl.endpoint(
        endpoint: String,
        id: String? = null,
        loadBalance: Boolean = true,
        subPaths: Boolean = true,
        rewritePath: Boolean = true,
        init: PredicateSpec.() -> Unit
    ) = this.route(id = id ?: "local-$endpoint", uri = if (loadBalance) endpoint.loadBalance() else endpoint) {
        host(LOCALHOST) and path(if (subPaths) endpoint.withSubPaths() else endpoint)
        init.invoke(this)
        if (rewritePath) {
            filters {
                rewritePath(endpoint.toSegmentRegex(), REPLACE_SEGMENT).filter(headerFactory.apply(HeaderConfig(API)))
            }
        }
        build()
    }

    private fun String.loadBalance() = "lb://$this"
    private fun String.toSegmentRegex() = "/$this(?<segment>/?.*)"
    private fun String.withSubPaths() = "/$this/**"
}

