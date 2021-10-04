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
import pl.emil.users.security.model.tokenize
import pl.emil.users.security.token.JwtSigner
import pl.emil.users.web.Token
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers.boundedElastic
import java.time.LocalDateTime.now
import java.util.*

@Service
class UserService(
    private val repository: UserRepository,
    private val signer: JwtSigner,
    private val env: Environment,
    @Lazy private var encoder: PasswordEncoder,
) : ReactiveUserDetailsService {

    fun getAll(): Flux<User> = repository.findAll()

    fun getAllAsXML(): Mono<UsersXML> = Flux.from(repository.findAll())
        .publishOn(boundedElastic())
        .collectList()
        .let { users -> users.map { xml -> UsersXML(xml) } }

    fun getOne(id: UUID): Mono<User?> = repository.findById(id)

    @Transactional
    fun create(user: User): Mono<User> = with(user) {
        repository.existsByEmail(email).flatMap { exist ->
            if (!exist) {
                user.run {
                    this.id = UUID.randomUUID()
                    this.password = encoder.encode(password)
                    this.createdAt = now()
                    repository.save(this)
                }
            } else Mono.just(user)
        }
    }

    override fun findByUsername(username: String): Mono<UserDetails?> =
        repository.findByEmail(username).map { user ->
            user?.let {
                SecureUser(it)
            } ?: userWithEmailNotFound(username)
        }

    fun getToken(username: String): Mono<Token> =
        repository.existsByEmail(username).map { doExist ->
            if (doExist) SecureUser(username).tokenize(signer, env)
            else userWithEmailNotFound(username)
        }

    private fun userWithEmailNotFound(username: String): Nothing =
        throw UsernameNotFoundException("User with email '$username' doesn't exist")

}
