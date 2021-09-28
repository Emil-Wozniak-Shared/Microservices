package pl.emil.users

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UsersApplicationTests: FunSpec({
    test("context should loads") {
        this shouldNotBe null
    }
})
