package pl.emil.gateway.utils

import org.springframework.http.HttpHeaders

class HeaderUtil {
}

fun HttpHeaders.notContainsKey(key: String) = !this.containsKey(key)
fun HttpHeaders.isNotAuthorized() = !this.containsKey(HttpHeaders.AUTHORIZATION)
