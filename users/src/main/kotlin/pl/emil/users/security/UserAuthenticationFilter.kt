package pl.emil.users.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import pl.emil.users.security.model.SecureUser
import pl.emil.users.service.UserService
import java.nio.file.attribute.UserPrincipalNotFoundException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class UserAuthenticationFilter(
    private val service: UserService
) : UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication =
        with(ObjectMapper().readValue<SecureUser>(request.inputStream)) {
            try {
                authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(username, password, authorities)
                )
            } catch (e: Exception) {
                throw UserPrincipalNotFoundException(e.message)
            }
        }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
//        super.successfulAuthentication(request, response, chain, authResult)
        val username = authentication.credentials().username
        service.loadUserByUsername(username)
    }
}

private fun Authentication.credentials() = this.principal as User