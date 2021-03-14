package pl.emil.users.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.emil.users.model.User
import pl.emil.users.model.UserXMLDTO
import pl.emil.users.repo.UserRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.empty
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

@Service
class UserService(private val repository: UserRepository) {

    fun all(): Flux<User> = repository.findAll()

    fun allAsXML(): Flux<UserXMLDTO> {
        val all = repository.findAll()
        val x = all.map { user -> UserXMLDTO(user) }

        return Flux.from(x)
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
}