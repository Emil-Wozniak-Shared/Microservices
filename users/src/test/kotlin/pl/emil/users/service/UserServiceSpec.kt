package pl.emil.users.service

import io.kotest.core.spec.style.FeatureSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.security.crypto.password.PasswordEncoder
import pl.emil.users.model.User
import pl.emil.users.repo.UserRepository
import pl.emil.users.security.token.JwtSigner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import java.util.UUID.randomUUID

@SpringBootTest
class UserServiceSpec(private val env: Environment) : FeatureSpec({

    val repository = spyk<UserRepository>()
    val encoder = mockk<PasswordEncoder>()
    val signer = mockk<JwtSigner>()
    val target = UserService(repository, signer, env, encoder)
    val user = createUser()
    val uuid = randomUUID()

    beforeAny {
        every { encoder.encode(any()) } returns ""
    }
    feature("User Service") {
        scenario("should can get all users") {
            every { repository.findAll() } returns Flux.just(user)
            target.getAll()
            verify(exactly = 1) { repository.findAll() }
        }

        scenario("should can get one user") {
            every { repository.findById(any<UUID>()) } returns Mono.just(user)
            target.getOne(uuid)
            verify(exactly = 1) { repository.findById(uuid) }
        }

        scenario("should successful user create") {
            every {
                repository.findByEmail(user.email)
            } returns Mono.just(user)
            every {
                repository.save(user)
            } returns Mono.just(user)
            target.create(user)
            verify {
                repository.findByEmail(eq(user.email))
                repository.save(eq(user))
            }
        }

        scenario("should can find user be Username") {
            val username = "alex"
            every { repository.findByEmail(username) } returns Mono.just(user)
            target.findByUsername(username)
            verify(exactly = 1) { repository.findByEmail(username) }
        }
    }
})


private fun createUser() = User(null, "test", "test", "1234", "test@test.pl")

