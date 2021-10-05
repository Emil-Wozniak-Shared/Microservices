package pl.emil.posts.security.token

import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import pl.emil.common.security.token.CommonJwtSigner

@Service
class JwtSigner(private val environment: Environment) : CommonJwtSigner(environment)
