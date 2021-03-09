package pl.emil.microservices.model.user

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
data class User(
    @Id
    var id: UUID? = UUID.randomUUID(),
    @NotNull
    @Size(min = 4, max = 7)
    var firstName: String? = null,
    @NotNull
    @Size(min = 4, max = 7)
    var lastName: String? = null,
    var email: @Email String? = null
)
