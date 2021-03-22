package pl.emil.users.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

//data class JwtAuthenticationToken constructor(
//    val principals: Any? = null,
//    val credential: Any? = null,
//    val roles: Collection<GrantedAuthority>? = mutableListOf()
//) : UsernamePasswordAuthenticationToken(principals, credential, roles) {
//    init {
//        super.setAuthenticated(false)
//        isAuthenticated = false
//    }
//
//    constructor(
//        jws: Jws<Claims>,
//        authentication: Authentication,
//        authorities: Collection<GrantedAuthority>
//    ) : this(jws.body.subject, authentication.credentials as String, authorities)
//
//}

class JWTAuthenticationToken(private val jws: Jws<Claims>) : Authentication {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun setAuthenticated(isAuthenticated: Boolean) {
        TODO("not implemented")
    }

    override fun getName(): String = this.jws.body.subject

    override fun getCredentials(): Any = this.jws.body.getValue("scopes")

    override fun getPrincipal(): Any = this.jws.body.subject

    override fun isAuthenticated(): Boolean = true

    override fun getDetails(): Any {
        TODO("not implemented")
    }

}

