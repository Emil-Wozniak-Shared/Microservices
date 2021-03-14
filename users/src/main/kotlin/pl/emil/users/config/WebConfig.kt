package pl.emil.users.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.xml.Jaxb2XmlDecoder
import org.springframework.http.codec.xml.Jaxb2XmlEncoder
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
class WebConfig : WebFluxConfigurer {
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.registerDefaults(false)
        configurer.customCodecs().registerWithDefaultConfig(Jaxb2XmlDecoder())
        configurer.customCodecs().registerWithDefaultConfig(Jaxb2XmlEncoder())
    }
}