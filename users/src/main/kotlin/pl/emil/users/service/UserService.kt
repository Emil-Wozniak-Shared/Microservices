package pl.emil.users.service

import org.springframework.context.annotation.Lazy
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.emil.users.model.User
import pl.emil.users.model.UsersXML
import pl.emil.users.repo.UserRepository
import pl.emil.users.security.model.SecureUser
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers.boundedElastic
import java.time.LocalDateTime
import java.util.*

@Service
class UserService(
    private val repository: UserRepository,
    @Lazy
    private var encoder: PasswordEncoder
) : UserDetailsService {

    fun all(): Flux<User> = repository.findAll()

    fun allAsXML(): Mono<UsersXML> = Flux.from(repository.findAll())
        .publishOn(boundedElastic())
        .collectList()
        .let {
            it.map { e ->
                UsersXML(e)
            }
        }

    fun one(id: UUID): Mono<User> = repository.findById(id)

    @Transactional
    fun create(user: User): Mono<User> =
        with(user) {
            this.password = encoder.encode(password)
            this.createdAt = LocalDateTime.now()
            repository.save(this)
        }

    override fun loadUserByUsername(username: String): UserDetails =
        with(repository.findByEmail(username)) {
            if (this != null) SecureUser(this)
            else throw UsernameNotFoundException("User with username: $username not found")
        }

    fun encode(password: String): String = encoder.encode(password)!!
}