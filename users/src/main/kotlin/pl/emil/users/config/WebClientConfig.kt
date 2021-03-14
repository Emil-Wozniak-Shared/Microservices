package pl.emil.users.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.xml.Jaxb2XmlDecoder
import org.springframework.http.codec.xml.Jaxb2XmlEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies.builder
import org.springframework.web.reactive.function.client.WebClient

//@Configuration
class WebClientConfig {
//    @Bean
//    fun webClient(): WebClient = WebClient
//        .builder()
//        .baseUrl("localhost:8040")
//        .exchangeStrategies(
//            builder()
//                .codecs { configurer: ClientCodecConfigurer ->
//                    configurer.defaultCodecs().jaxb2Encoder(Jaxb2XmlEncoder())
//                    configurer.defaultCodecs().jaxb2Decoder(Jaxb2XmlDecoder())
//                }.build()
//        )
//        .build()
}