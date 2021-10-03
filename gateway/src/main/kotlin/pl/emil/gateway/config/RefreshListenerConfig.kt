package pl.emil.gateway.config

import org.springframework.cloud.gateway.event.RefreshRoutesResultEvent
import org.springframework.cloud.gateway.route.CachingRouteLocator
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RefreshListenerConfig {
    @Bean
    fun routesRefreshed() = ApplicationListener<RefreshRoutesResultEvent> {
        println("Routes update")
        (it.source as CachingRouteLocator).routes.subscribe(::println)
    }
}
