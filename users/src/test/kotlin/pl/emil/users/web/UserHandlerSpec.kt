package pl.emil.users.web

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.extensions.testcontainers.perTest
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType
import org.springframework.http.MediaType.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.bindToApplicationContext
import org.springframework.test.web.reactive.server.body
import org.testcontainers.containers.GenericContainer
import pl.emil.users.model.UserCredentials
import pl.emil.users.model.UsersXML
import reactor.core.publisher.Mono
import java.time.Duration.ofMinutes

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
internal class UserHandlerSpec(var context: ApplicationContext) : BehaviorSpec({
//    val psql = GenericContainer<Nothing>("postgres:9.6.23").apply {
//        this.env = listOf(
//            "loggerLevel=OFF",
//            "POSTGRES_DB=test",
//            "POSTGRES_USER=test",
//            "POSTGRES_PASSWORD=test"
//        )
//    }
//    listener(psql.perSpec())
    val mapper = ObjectMapper()
    beforeEach {
        mapper.findAndRegisterModules()
    }
    val uri = "/api/users/"
    val client: WebTestClient = bindToApplicationContext(context)
        .configureClient()
        .responseTimeout(ofMinutes(1))
        .build()

    given("endpoints") {
        `when`("get all should ") {
            then("return status ok") {
                client.get().performRequest(uri).exchange().expectStatus().isOk
            }
        }
        `when`("get all with content type JSON") {
            then("should be list") {
                client.get().performRequest(uri).exchange().expectBody(List::class.java)
            }
        }
        `when`("get all with content type XML") {
            then("should be list") {
                client.get().performRequest(uri, APPLICATION_XML, APPLICATION_ATOM_XML_VALUE).exchange()
                    .expectBody(UsersXML::class.java)
            }
        }
        `when`("can obtain own user details") {
            then("when logged in") {
                client.post().performRequest(uri)
                    .body<UserCredentials>(Mono.just(UserCredentials("new@example.com", "pw")))
                    .exchange()
                    .expectStatus().isNoContent
                    .expectHeader().exists("Set-Cookie")
                    .expectHeader().valueMatches("Set-Cookie", "X-Auth.*; Path=/; Max-Age=3600; Expires=.*; HttpOnly")
                    .expectBody()
                    .consumeWith {
                        println(it)
                    }
            }
        }
    }
})

fun <S : WebTestClient.RequestHeadersSpec<S>> WebTestClient.RequestHeadersUriSpec<S>.performRequest(
    uri: String,
    accept: MediaType = APPLICATION_JSON,
    contentType: String = APPLICATION_JSON_VALUE,
) = this.uri(uri).header(CONTENT_TYPE, contentType).accept(accept)

