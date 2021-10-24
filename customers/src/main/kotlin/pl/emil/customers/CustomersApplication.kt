package pl.emil.customers

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP
import org.springdoc.core.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class CustomersApplication {
    // http://localhost:8060/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
    @Bean
    open fun customOpenAPI(@Value("\${springdoc.version}") appVersion: String?): OpenAPI? {
        return OpenAPI()
            .components(Components().addSecuritySchemes("basicScheme",
                SecurityScheme().type(HTTP).scheme("basic")))
            .info(Info().title("Tweet API").version(appVersion)
                .license(License().name("Apache 2.0").url("http://springdoc.org")))
    }

    @Bean
    fun storeOpenApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("user")
            .pathsToMatch("/**")
            .build()
    }

    @Bean
    fun userOpenApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("x-stream")
            .pathsToMatch("/stream/**")
            .packagesToScan("org.springdoc.demo.app3")
            .build()
    }
}

fun main(args: Array<String>) {
    runApplication<CustomersApplication>(*args)
}
