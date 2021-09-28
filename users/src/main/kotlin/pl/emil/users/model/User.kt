package pl.emil.users.model

import org.springframework.core.env.Environment
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.userdetails.UserDetails
import pl.emil.users.config.LocalDateTimeAdapter
import pl.emil.users.security.token.JwtSigner
import pl.emil.users.web.Token
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

/**
 * https://www.dariawan.com/tutorials/java/using-jaxb-java-11/
 */
@Table(value = "users")
@XmlRootElement(name = "user")
@XmlType(
    name = "user",
    propOrder = ["id", "firstName", "lastName", "email", "karma", "createdAt", "updatedAt", "version"]
)
data class User(
    @Id
    @Column(value = "id")
    var id: UUID? = UUID.randomUUID(),

    @Column(value = "first_name")
    var firstName: String = "",

    @Column(value = "last_name")
    var lastName: String = "",

    @Column(value = "password")
    var password: String = "",

    @Column(value = "email")
    var email: String = "",

    @Column
    var karma: Short = 80,

    @Column(value = "created_at")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter::class)
    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @Column(value = "updated_at")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter::class)
    var updatedAt: LocalDateTime? = null,

    @Version
    @Column(value = "version")
    var version: Long? = null
)



