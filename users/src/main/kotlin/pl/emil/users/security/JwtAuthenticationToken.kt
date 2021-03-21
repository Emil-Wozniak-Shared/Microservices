package pl.emil.users.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    principal: Any,
    credentials: Any,
    authorities: Collection<GrantedAuthority>
) : UsernamePasswordAuthenticationToken(principal, credentials, authorities)