package pl.emil.microservices.repo

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.emil.microservices.model.user.User
import java.util.*

interface UserRepository : ReactiveCrudRepository<User, UUID>