package pl.emil.users.model

import java.time.LocalDateTime
import java.util.*
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class UserXMLDTO constructor(
    var id: UUID? = UUID.randomUUID(),
    var firstName: String = "",
    var lastName: String = "",
    var password: String = "",
    var email: String = "",
    var karma: Short = 80,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var version: Long? = null
) {
    constructor(user: User) : this() {
        val (id, firstName, lastName, password, email, karma) = user
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.password = password
        this.email = email
        this.karma = karma
    }
}