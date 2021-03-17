package pl.emil.users.security.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import pl.emil.users.model.User

data class SecureUser(
    // inherits from UserDetails
    private var username: String = "",
    private val password: String= "",
    private val isEnabled: Boolean = false, //Disabled account can not log in
    private val isCredentialsNonExpired: Boolean = false, //credential can be expired,eg. Change the password every three months
    private val isAccountNonExpired: Boolean = false, //eg. Demo account（guest） can only be online  24 hours
    private val isAccountNonLocked: Boolean = false, //eg. Users who malicious attack system,lock their account for one year
    private val authorities: Set<GrantedAuthority> = mutableSetOf(),
) : UserDetails {
    override fun getUsername(): String = username
    override fun getPassword(): String = password
    override fun isEnabled(): Boolean = isEnabled
    override fun isCredentialsNonExpired(): Boolean = isCredentialsNonExpired
    override fun isAccountNonExpired(): Boolean = isAccountNonExpired
    override fun isAccountNonLocked(): Boolean = isAccountNonLocked
    override fun getAuthorities(): Set<GrantedAuthority> = authorities

    constructor(user: User): this() {
        username = user.email
    }
}