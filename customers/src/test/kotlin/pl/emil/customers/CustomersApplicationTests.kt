package pl.emil.customers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CustomersApplicationTests : FunSpec({
    test("context loads") {
        this shouldNotBe null
    }
})
