package pl.emil.microservices.repo

import org.springframework.data.jpa.repository.JpaRepository
import pl.emil.microservices.model.user.User
import java.util.*

interface UserRepository : JpaRepository<User, UUID>