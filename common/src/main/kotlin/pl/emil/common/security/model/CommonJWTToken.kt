package pl.emil.common.security.model

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

open class CommonJWTToken(private val jws: Jws<Claims>) : Authentication {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun setAuthenticated(isAuthenticated: Boolean) {
        TODO("not implemented")
    }

    override fun getName(): String = this.jws.body.subject

    override fun getCredentials(): Any = this.jws.body.getValue("scopes")

    override fun getPrincipal(): Any = this.jws.body.subject

    override fun isAuthenticated(): Boolean = true

    override fun getDetails(): Any = this.jws.signature

}

