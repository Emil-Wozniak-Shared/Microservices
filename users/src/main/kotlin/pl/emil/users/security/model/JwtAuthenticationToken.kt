package pl.emil.users.security.model

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import pl.emil.common.security.model.CommonJWTToken

class JWTAuthenticationToken(private val jws: Jws<Claims>) : CommonJWTToken(jws)
