package pl.emil.users.security.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import pl.emil.users.model.User

/**
 * inherits from UserDetails
 */
data class SecureUser(
    private var username: String = "",
    private var password: String = "",
    /**
     * Disabled account can not log in
     */
    private val isEnabled: Boolean = true,
    /**
    * credential can be expired,eg. Change the password every three months
    */
    private val isCredentialsNonExpired: Boolean = true,
    /**
    * eg. Demo account（guest） can only be online  24 hours
    */
    private val isAccountNonExpired: Boolean = true,
    /**
    * eg. Users who malicious attack system,lock their account for one year
    */
    private val isAccountNonLocked: Boolean = true,
    private val authorities: Set<GrantedAuthority> = mutableSetOf(),
) : UserDetails {
    override fun getUsername(): String = username
    override fun getPassword(): String = password
    override fun isEnabled(): Boolean = isEnabled
    override fun isCredentialsNonExpired(): Boolean = isCredentialsNonExpired
    override fun isAccountNonExpired(): Boolean = isAccountNonExpired
    override fun isAccountNonLocked(): Boolean = isAccountNonLocked
    override fun getAuthorities(): Set<GrantedAuthority> = authorities

    constructor(user: User) : this() {
        user.let {
            username = it.email
            password = it.password
        }
    }
}