package pl.emil.gateway

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GatewayApplicationTests: FunSpec({
    test("context loads") {
        this shouldNotBe null
    }
})
