package pl.emil.microservices

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe

class MicroservicesApplicationSpec : FunSpec({
    test("context loads") {
        this shouldNotBe null
    }
})
