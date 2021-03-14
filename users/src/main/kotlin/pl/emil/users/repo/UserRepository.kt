package pl.emil.users.repo

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.emil.users.model.User
import pl.emil.users.model.UserXMLDTO
import reactor.core.publisher.Flux
import java.util.*

interface UserRepository: ReactiveCrudRepository<User, UUID> {
}