package pl.emil.pdf.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.*
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchangeOrNull
import org.springframework.web.reactive.function.client.bodyToFlux
import pl.emil.contract.model.Token
import pl.emil.contract.model.User
import pl.emil.contract.model.Login

const val USERNAME = "a.wozniak@test.pl"

@Component
class UserClient {

    @Autowired
    private lateinit var webClient: WebClient
    suspend fun getToken(): String {
        return webClient.post()
            .uri("/users/oauth/token")
            .bodyValue(Login(USERNAME))
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .retrieve()
            .awaitBody<Token>()
            .token ?: ""
    }

    suspend fun getUsers(retrieve: (value: Any) -> Unit) {
        val token = getToken()
        webClient.get()
            .uri("/users/api/users")
            .header(AUTHORIZATION, token)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .awaitExchangeOrNull { response ->
                response.bodyToFlux<User>()
                    .collectList()
                    .subscribe {
                        retrieve.invoke(it)
                    }
            }
    }
}
