package pl.emil.microservices.web.handlers

import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.*
import pl.emil.microservices.model.user.User
import pl.emil.microservices.repo.UserRepository
import pl.emil.microservices.util.pageRequest
import java.net.URI

@Service
class UserHandler(private val repo: UserRepository) : ApiHandler<User> {
    override fun all(request: ServerRequest): ServerResponse =
        pageRequest(request) {
            val content = repo.findAll(it)
            ok().body(content)
        }

    override fun getOne(request: ServerRequest): ServerResponse =
        request
            .let {
                val id = it.pathVariable("id")
                val entity = repo.findById(id.toUUID())
                ok().contentType(it.mediaType()).body(entity)
            }

    override fun create(request: ServerRequest): ServerResponse =
        request.body(User::class.java)
            .let(repo::save)
            .let { created(URI.create(it.id.toString())).body(it) }

    override fun update(request: ServerRequest): ServerResponse =
        ok().body(request.body(User::class.java))

    override fun delete(request: ServerRequest): ServerResponse =
        this.repo.findById(request.id()).let {
            it.ifPresent { user ->
                this.repo.delete(user)
            }
            status(NO_CONTENT).build()
        }

}