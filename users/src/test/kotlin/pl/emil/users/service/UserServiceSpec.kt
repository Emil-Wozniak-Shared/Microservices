package pl.emil.users.service

import io.kotest.core.spec.style.FeatureSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder
import pl.emil.users.model.User
import pl.emil.users.repo.UserRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import java.util.UUID.randomUUID

class UserServiceSpec : FeatureSpec({

    val repository = mockk<UserRepository>()
    val encoder = mockk<PasswordEncoder>()
    val target = UserService(repository, encoder)
    val user = createUser()
    val uuid = randomUUID()

    beforeAny {
        every { encoder.encode(any()) } returns ""
    }
    feature("User Service") {
        scenario("should can get all users") {
            every { repository.findAll() } returns Flux.just(user)
            target.all()
            verify(exactly = 1) { repository.findAll() }
        }

        scenario("should can get one user") {
            every { repository.findById(any<UUID>()) } returns Mono.just(user)
            target.one(uuid)
            verify(exactly = 1) { repository.findById(uuid) }
        }

        scenario("should successful user create") {
            every { repository.save(any()) } returns Mono.just(user)
            target.create(user)
            verify(exactly = 1) { repository.save(user) }
        }

        scenario("should can find user be Username") {
            val username = "alex"
            every { repository.findByEmail(username) } returns Mono.just(user)
            target.findByUsername(username)
            verify(exactly = 1) { repository.findByEmail(username) }
        }
    }
})


private fun createUser() = User(null, "a", "a")

