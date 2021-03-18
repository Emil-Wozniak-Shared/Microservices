package pl.emil.users.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.bindToApplicationContext
import org.springframework.test.web.reactive.server.body
import org.springframework.web.reactive.function.BodyInserters
import pl.emil.users.model.User
import pl.emil.users.model.UserCredentials
import pl.emil.users.model.UsersXML
import reactor.core.publisher.Mono
import java.time.Duration.ofMinutes
import java.time.LocalDateTime
import java.util.*

@TestInstance(PER_CLASS)
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
internal class UserHandlerTest {

    @Suppress("PrivatePropertyName")
    private val URL = "/api/users"

    private val mapper: ObjectMapper = ObjectMapper()

    @Autowired
    private lateinit var context: ApplicationContext
    private lateinit var client: WebTestClient

    @BeforeEach
    internal fun setUp() {
        mapper.findAndRegisterModules()
        client = bindToApplicationContext(context)
            .configureClient()
            .responseTimeout(ofMinutes(1))
            .build()
    }

    @Test
    fun `get all should return status ok`() {
        client.get()
            .uri(URL)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun `get all with content type JSON should be list`() {
        client.get()
            .uri(URL)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectBody(List::class.java)
    }

    @Test
    fun `get all with content type XML should be list`() {
        client.get()
            .uri(URL)
            .header(CONTENT_TYPE, APPLICATION_XML_VALUE)
            .accept(APPLICATION_XML)
            .exchange()
            .expectBody(UsersXML::class.java)

    }

    @Test
    fun `can obtain own user details when logged in`() {
        client.post()
            .uri("/api/login")
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON)
            .body<UserCredentials>(Mono.just(UserCredentials("new@example.com", "pw")))
            .exchange()
            .expectStatus().isNoContent
            .expectHeader().exists("Set-Cookie")
            .expectHeader().valueMatches("Set-Cookie","X-Auth.*; Path=/; Max-Age=3600; Expires=.*; HttpOnly")
            .expectBody()
            .consumeWith {
                println(it)
            }
    }
}