package pl.emil.microservices.model.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Table(value = "users")
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
