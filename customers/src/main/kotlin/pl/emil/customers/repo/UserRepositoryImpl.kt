package pl.emil.customers.repo

import org.springframework.stereotype.Repository
import pl.emil.customers.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.annotation.PostConstruct

@Repository
class UserRepositoryImpl : UserRepository {
    private val users: MutableMap<Long, User?> = mutableMapOf()

    @PostConstruct
    @Throws(Exception::class)
    fun init() {
        users[1L] = User(1, "Jack", "Smith", 20)
        users[2L] = User(2, "Peter", "Johnson", 25)
    }

    override fun getUserById(id: Long): Mono<User?> = users[id]?.let { Mono.just(it) } ?: Mono.empty()
    override fun getAll(): Flux<User> = Flux.fromStream {
        users.values
            .stream()
            .peek {
                println(it)
            }
    }

    override fun getAllUsers(firstname: String): Flux<User?> =
        Flux.fromIterable(users.values.filter { it!!.firstname == firstname })

    override fun saveUser(user: Mono<User>): Mono<Void> =
        user
            .doOnNext {
                users[it.id] = it
                println("########### POST:$it")
            }
            .then()

    override fun putUser(id: Long, user: Mono<User>): Mono<User?> = user.doOnNext {
        it.id = id
        users[id] = it
        println("########### PUT:$it")
    }

    override fun deleteUser(id: Long): Mono<String> =
        Mono
            .just("Delete Successfully!")
            .doOnNext {
                users.remove(id)
            }
}
