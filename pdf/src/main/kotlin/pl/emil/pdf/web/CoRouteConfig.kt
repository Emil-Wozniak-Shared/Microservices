package pl.emil.pdf.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter
import pl.emil.pdf.handler.FlyingPDFHandler
import pl.emil.pdf.handler.FopPDFHandler

@Configuration
class CoRouteConfig {
    @Bean("coRoutes")
    fun routes(fly: FlyingPDFHandler, fop: FopPDFHandler) =
        coRouter {
            "api".nest {
                "pdf".nest {
                    GET("/fly", fly::getPdf)
                    GET("/fop", fop::createLines)
                }
            }
        }
}
