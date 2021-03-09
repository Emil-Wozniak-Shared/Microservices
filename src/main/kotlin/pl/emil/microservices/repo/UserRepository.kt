package pl.emil.microservices.repo

import org.springframework.data.r2dbc.repository.R2dbcRepository
import pl.emil.microservices.model.user.User
import java.util.*

interface UserRepository : R2dbcRepository<User, UUID>