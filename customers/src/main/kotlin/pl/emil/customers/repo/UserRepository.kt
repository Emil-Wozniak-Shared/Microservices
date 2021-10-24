package pl.emil.customers.repo

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn.PATH
import io.swagger.v3.oas.annotations.tags.Tag
import pl.emil.customers.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Tag(name = "Users")
interface UserRepository {
    fun getUserById(@Parameter(`in` = PATH, description = "The user Id") id: Long): Mono<User?>

    @Operation(description = "get all the users")
    fun getAll(): Flux<User>

    @Operation(description = "get all the users by firstname")
    fun getAllUsers(firstname: String): Flux<User?>
    fun saveUser(user: Mono<User>): Mono<Void>
    fun putUser(@Parameter(`in` = PATH) id: Long, user: Mono<User>): Mono<User?>
    fun deleteUser(@Parameter(`in` = PATH) id: Long): Mono<String>
}
