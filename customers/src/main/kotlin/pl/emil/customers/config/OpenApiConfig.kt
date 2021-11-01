package pl.emil.customers.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    // http://localhost:8060/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
    @Bean
    fun customOpenAPI(): OpenAPI? = OpenAPI()
        .components(securityBasicHTTP())
        .info(
            Info()
                .title("Customers API")
                .version("1.0")
                .license(apache2())
        )

    @Bean
    fun storeOpenApi(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("user")
        .pathsToMatch("/api/user/**")
        .build()

    @Bean
    fun userOpenApi(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("customers")
        .pathsToMatch("/api/customers/*")
        .packagesToScan("pl/emil/customers/service")
        .build()

    private fun securityBasicHTTP() = Components()
        .addSecuritySchemes(
            "basicScheme",
            SecurityScheme()
                .type(HTTP)
                .scheme("basic")
        )

    private fun apache2() = License()
        .name("Apache 2.0")
        .url("https://www.apache.org/licenses/LICENSE-2.0")
}
