package pl.emil.users.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.emil.users.model.User
import pl.emil.users.model.UsersXML
import pl.emil.users.repo.UserRepository
import pl.emil.users.security.model.SecureUser
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.empty
import reactor.core.scheduler.Schedulers.boundedElastic
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

@Service
class UserService(private val repository: UserRepository) : UserDetailsService {

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
    fun create(user: Mono<User>): Mono<User> {
        val success = AtomicBoolean(true)
        return user
            .doOnNext {
                val subscribe = repository.save(it).subscribe()
                success.set(subscribe.isDisposed)
            }
            .flatMap {
                if (success.get()) Mono.just(it)
                else empty()
            }
    }

    override fun loadUserByUsername(username: String): UserDetails =
        with(repository.findByEmail(username)) {
            SecureUser(this)
        }
}