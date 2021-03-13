package pl.emil.users.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.LocalDateTime.*
import java.util.*
import javax.validation.constraints.Email

@Table(value = "customers")
data class User(
    @Id
    @Column(value = "id")
    var id: UUID = UUID.randomUUID(),

    @Column(value = "first_name")
    var firstName: String = "",

    @Column(value = "last_name")
    var lastName: String = "",

    @Email
    @Column(value = "email")
    var email: String = "",

    @Column
    var karma: Short = 80,

    @Column(value = "created_at")
    var createdAt: LocalDateTime = now()

)