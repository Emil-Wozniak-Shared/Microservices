package pl.emil.users.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*
import javax.xml.bind.annotation.XmlAccessType.FIELD
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType

/**
 * https://www.dariawan.com/tutorials/java/using-jaxb-java-11/
 */
@Table(value = "users")
@XmlAccessorType(FIELD)
//@XmlRootElement(name = "user")
@XmlType(name="user")
data class User(
    @Id
    @Column(value = "id")
    var id: UUID? = UUID.randomUUID(),

    @XmlAttribute
    @Column(value = "first_name")
    var firstName: String = "",

    @XmlAttribute
    @Column(value = "last_name")
    var lastName: String = "",

    @Column(value = "email")
    var email: String = "",

    @Column
    var karma: Short = 80,

    @Column(value = "created_at")
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(value = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @Version
    @Column(value = "version")
    var version: Long? = null
) {

    fun jsonValue(): String =
        "{" +
                "\"id\":\"$id\"," +
                "\"firstName\":\"$firstName\"," +
                "\"lastName\":\"$lastName\"," +
                "\"email\":\"$email\"," +
                "\"karma\":$karma," +
                "\"createdAt\":\"${createdAt.toString()}\"," +
                "\"updatedAt\":${updatedAt.toString()}," +
                "\"version\":$version" +
                "}"
}