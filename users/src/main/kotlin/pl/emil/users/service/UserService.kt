package pl.emil.users.service

import org.springframework.context.annotation.Lazy
import org.springframework.core.env.Environment
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.emil.users.model.User
import pl.emil.users.model.UsersXML
import pl.emil.users.repo.UserRepository
import pl.emil.users.security.model.SecureUser
import pl.emil.users.security.token.JwtSigner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers.boundedElastic
import java.time.LocalDateTime
import java.util.*

@Service
class UserService(
    private val repository: UserRepository,
    @Lazy
    private var encoder: PasswordEncoder,
) : ReactiveUserDetailsService {

    fun all(): Flux<User> = repository.findAll()

    fun allAsXML(): Mono<UsersXML> = Flux.from(repository.findAll())
        .publishOn(boundedElastic())
        .collectList()
        .let { users -> users.map { xml -> UsersXML(xml) } }

    fun one(id: UUID): Mono<User> = repository.findById(id)

    @Transactional
    fun create(user: User): Mono<User> =
        with(user) {
            this.password = encoder.encode(password)
            this.createdAt = LocalDateTime.now()
            repository.save(this)
        }

    override fun findByUsername(username: String): Mono<UserDetails?> =
        repository.findByEmail(username)?.map {
            SecureUser(it)
        } ?: Mono.error(UsernameNotFoundException("User with username: $username not found"))
}
