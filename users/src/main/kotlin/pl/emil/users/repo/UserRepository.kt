package pl.emil.users.repo

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.emil.users.model.User
import java.util.*

interface UserRepository : ReactiveCrudRepository<User, UUID> {

    fun findByEmail(email: String): User?
}