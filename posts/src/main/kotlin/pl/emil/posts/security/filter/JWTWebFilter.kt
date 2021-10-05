package pl.emil.posts.security.filter

import org.springframework.security.authentication.ReactiveAuthenticationManager
import pl.emil.common.security.filter.CommonJWTWebFilter

class JWTWebFilter(private val authenticationManager: ReactiveAuthenticationManager) : CommonJWTWebFilter(authenticationManager)

