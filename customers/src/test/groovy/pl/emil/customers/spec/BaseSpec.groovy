package pl.emil.customers.spec

import org.junit.jupiter.api.BeforeEach
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import pl.emil.customers.CustomersApplication
import pl.emil.customers.model.Customer
import pl.emil.customers.repo.CustomerRepo
import pl.emil.customers.web.CustomerController
import spock.lang.Specification

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup

@AutoConfigureJson
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
@SpringBootTest(classes = [CustomersApplication])
abstract class BaseSpec extends Specification {

    @SpringBean
    CustomerRepo repo = Mock()

    @Autowired
    private CustomerController controller

    @Autowired
    public MockMvc mockMvc

    @BeforeEach
    def setup() {
        def bob = new Customer(2L, "Bob")
        def jane = new Customer(1L, "Jane")
        repo.findAll() >> [jane, bob]
        repo.findById(2L) >> Optional.of(bob)
        given().mockMvc(mockMvc)
        standaloneSetup(controller)
    }
}
