package pl.emil.users.containers

import io.kotest.core.spec.style.AnnotationSpec.BeforeClass
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.stereotype.Component
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait.forHttp
import org.testcontainers.utility.DockerImageName

//@Component
//@TestInstance(PER_CLASS)
class PsqlContainer {

//    companion object {
//        @JvmStatic
//        @BeforeClass
//        var psql: PostgreSQLContainer<Nothing> =
//            PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:9.6.8")).apply {
//                this.setWaitStrategy(
//                    forHttp("/").forStatusCodeMatching { it in 200..299 || it == 401 }
//                )
//                this.withExposedPorts(6444)
//                this.withReuse(true)
//                this.withDatabaseName("test")
//                this.withUsername("test")
//                this.withPassword("test")
//            }
//    }

//    val dao: PsqlDao = PsqlDao(psql.jdbcUrl, psql.username, psql.password)
//    data class PsqlDao(val jdbcUrl: String, val username: String, val password: String)


    //        @JvmStatic
//        @DynamicPropertySource
//        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
//            registry.add("spring.database.url", container::getJdbcUrl )
//            registry.add("spring.r2dbc.username", container::getUsername)
//            registry.add("spring.r2dbc.password", container::getPassword)
//        }


}

