package pl.emil.gateway.config

import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.route.builder.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.emil.gateway.factory.HeaderGatewayFilterFactory
import pl.emil.gateway.factory.HeaderGatewayFilterFactory.HeaderConfig
import java.util.concurrent.atomic.AtomicBoolean

@Configuration
class SpringCloudConfig(private val headerFactory: HeaderGatewayFilterFactory) {

    private val isWs: AtomicBoolean = AtomicBoolean(false)

    @Bean
    @RefreshScope
    fun additionalRouteLocator(builder: RouteLocatorBuilder) =
        builder.routes {
            endpoint("users") {}
            endpoint("posts") {}
            route {
                localhost() and path("/error/**")
                uri(loadBalance("customers"))
                filters {
                    retry(8)
                }
                build()
            }
            if (!isWs.get()) {
                isWs.set(true)
                endpoint("customers") {}
            } else endpoint("customers", useWebSocket = true) {}
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
        useWebSocket: Boolean = false,
        init: PredicateSpec.() -> Unit,
    ) = this.route(id = id ?: "local-$endpoint", uri = if (loadBalance) loadBalance(endpoint) else endpoint) {
        localhost() and path(if (subPaths) endpoint.withSubPaths() else endpoint)
        init.invoke(this)
        if (rewritePath) {
            filters {
                if (useWebSocket) path("/ws/$endpoint")
                else rewritePath(endpoint.toSegmentRegex(), REPLACE_SEGMENT).filter(headerFactory.apply(HeaderConfig(API)))
            }
        }
        build()
    }

    private fun PredicateSpec.localhost() = host(LOCALHOST)
    private fun loadBalance(path: String) = "lb://$path"
    private fun String.toSegmentRegex() = "/$this(?<segment>/?.*)"
    private fun String.withSubPaths() = "/$this/**"
}

