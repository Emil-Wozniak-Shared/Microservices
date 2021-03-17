package pl.emil.users.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pl.emil.users.security.model.SecureUser
import java.nio.file.attribute.UserPrincipalNotFoundException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UserAuthenticationFilter : UsernamePasswordAuthenticationFilter() {
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
        authResult: Authentication?
    ) {
        super.successfulAuthentication(request, response, chain, authResult)
    }
}