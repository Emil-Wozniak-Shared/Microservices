package pl.emil.microservices.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryBuilder
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.Jsr310Converters.getConvertersToRegister
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories
class GatewayR2dbcConfiguration(
    private val r2dbcProperties: R2dbcProperties
) : AbstractR2dbcConfiguration() {
    @Bean
    override fun connectionFactory(): ConnectionFactory =
        ConnectionFactoryBuilder.of(r2dbcProperties, null).build()

    override fun getCustomConverters(): MutableList<Converter<*, *>> =
        getConvertersToRegister() as MutableList<Converter<*, *>>
}