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
import pl.emil.users.model.User
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

    private val user1 = User(
        id = UUID.fromString("7088ad19-43dd-4e4f-9309-629577a26c6f"),
        firstName = "Emil",
        lastName = "Woźniak",
        email = "e.wozniak@gmail.com",
        karma = 80,
        createdAt = LocalDateTime.parse("2021-03-14T10:43:03.460834"),
        version = 0
    )
    private val user2 = User(
        id = UUID.fromString("f8002219-fa1f-4928-9bbf-4e13a4a91f4f"),
        firstName = "Adrian",
        lastName = "Woźniak",
        email = "a.wozniak@gmail.com",
        karma = 80,
        createdAt = LocalDateTime.parse("2021-03-14T12:38:17.008058"),
        version = 0
    )
    private val users = listOf(user1, user2)

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
        val message = users.joinToString(prefix = "[", postfix = "]") { it.jsonValue() }
        client.get()
            .uri(URL)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON)
            .exchange().expectBody()
            .json(message)
    }

    @Test
    fun `get all with content type XML should be list`() {
        val xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><users><users/></users>"
        client.get()
            .uri(URL)
            .header(CONTENT_TYPE, APPLICATION_XML_VALUE)
            .accept(APPLICATION_XML)
            .exchange()
            .expectBody()
            .xml(xmlString)
    }

}