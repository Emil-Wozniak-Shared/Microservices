package pl.emil.customers.web

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import pl.emil.customers.model.Customer

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "50000")
class RouteConfigSpec(
    private val restTemplate: TestRestTemplate,
    private val client: WebTestClient,
    private val mapper: ObjectMapper
) : FeatureSpec({
    beforeSpec {
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
    var token: String?
    beforeEach {
        mapper.findAndRegisterModules()
    }
    val entity = HttpEntity(Login("a.wozniak@test.pl"), HttpHeaders().apply {
        set("Content-Type", MediaType.APPLICATION_JSON_VALUE)
    })
    feature("endpoints") {
        scenario("request all users should return status ok") {
            token = restTemplate.requestToken(entity)?.body?.token
            client.get().performRequest("/api/customers", token = token).exchange().returnResult<Customer>()
                .let { response ->
                    response.status shouldBe OK
                }
        }
    }
})

data class Login(val username: String)
data class Token(val token: String, val expiresIn: Int)

private fun TestRestTemplate.requestToken(
    entity: HttpEntity<Login>
) = this.postForEntity("/oauth/token", entity, Token::class.java)

fun <S : WebTestClient.RequestHeadersSpec<S>> WebTestClient.RequestHeadersUriSpec<S>.performRequest(
    uri: String,
    accept: MediaType = MediaType.APPLICATION_JSON,
    contentType: String = MediaType.APPLICATION_JSON_VALUE,
    token: String? = "",
) = this
    .uri(uri)
    .header(HttpHeaders.CONTENT_TYPE, contentType)
    .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
    .accept(accept)

