package pl.emil.common.config

import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.http.codec.xml.Jaxb2XmlDecoder
import org.springframework.http.codec.xml.Jaxb2XmlEncoder
import org.springframework.web.reactive.config.WebFluxConfigurer

interface CommonCodecWebConfig : WebFluxConfigurer {
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder())
        configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder())
        configurer.defaultCodecs().jaxb2Decoder(Jaxb2XmlDecoder())
        configurer.defaultCodecs().jaxb2Encoder(Jaxb2XmlEncoder())
    }
}
