package pl.emil.microservices.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.http.codec.xml.Jaxb2XmlDecoder
import org.springframework.http.codec.xml.Jaxb2XmlEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies.builder
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableR2dbcAuditing
internal class DataConfig {

    @Bean
    fun webClient(): WebClient = WebClient
        .builder()
        .baseUrl("localhost:8080")
        .exchangeStrategies(
            builder().codecs {
                it.defaultCodecs().jaxb2Encoder(Jaxb2XmlEncoder())
                it.defaultCodecs().jaxb2Decoder(Jaxb2XmlDecoder())
                it.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder())
                it.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder())
            }.build()
        )
        .build()
}