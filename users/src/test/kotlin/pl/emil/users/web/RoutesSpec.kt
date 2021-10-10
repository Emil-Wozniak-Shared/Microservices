package pl.emil.users.web

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.http.MediaType.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import pl.emil.users.model.Login
import pl.emil.users.model.Token
import pl.emil.users.model.User
import pl.emil.users.model.UsersXML

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "50000")
class RoutesSpec(
    private val restTemplate: TestRestTemplate,
    private val client: WebTestClient,
    private val mapper: ObjectMapper,
) : FeatureSpec({
    beforeSpec {
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
    val entity = HttpEntity(Login("alex"), HttpHeaders().apply {
        set("Content-Type", APPLICATION_JSON_VALUE)
    })
    var token: String?
    beforeEach {
        mapper.findAndRegisterModules()
    }
    val uri = "/api/users"
    feature("endpoints") {
        scenario("user should can obtain token") {
            val response = restTemplate.requestToken(entity)?.body
            with(response) {
                this!!.token shouldNotBe "[]"
                this.expiresIn shouldBe 740000
            }
        }
        scenario("request all users should return status ok") {
            token = restTemplate.requestToken(entity)?.body?.token
            client.get().performRequest("/api/users", token = token).exchange().returnResult<User>()
                .let { response ->
                    response.status shouldBe OK
                }
        }
        scenario("get all with content type JSON should be list") {
            client.get().performRequest(uri).exchange().expectBody(List::class.java)
        }
        scenario("get all with content type XML should be list") {
            client.get().performRequest(uri, APPLICATION_XML, APPLICATION_ATOM_XML_VALUE)
                .exchange()
                .expectBody(UsersXML::class.java)
        }
    }
})

private fun TestRestTemplate.requestToken(
    entity: HttpEntity<Login>
) = this.postForEntity("/oauth/token", entity, Token::class.java)

fun <S : WebTestClient.RequestHeadersSpec<S>> WebTestClient.RequestHeadersUriSpec<S>.performRequest(
    uri: String,
    accept: MediaType = APPLICATION_JSON,
    contentType: String = APPLICATION_JSON_VALUE,
    token: String? = "",
) = this
    .uri(uri)
    .header(CONTENT_TYPE, contentType)
    .header(AUTHORIZATION, "Bearer $token")
    .accept(accept)

