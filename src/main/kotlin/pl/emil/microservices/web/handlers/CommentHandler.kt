package pl.emil.microservices.web.handlers

import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.created
import org.springframework.web.servlet.function.ServerResponse.ok
import pl.emil.microservices.model.Comment
import pl.emil.microservices.repo.CommentRepository
import java.net.URI

@Component
class CommentHandler(private val comments: CommentRepository) {
    fun create(request: ServerRequest): ServerResponse {
        return request.body(Comment::class.java)
            .let { comment ->
                comment.postId = request.id()
                val save = this.comments.save(comment)
                created(URI.create("/posts/" + request.id() + "/comments/" + save.id)).build()
            }
    }

    fun getByPostId(req: ServerRequest): ServerResponse {
        val id = req.pathVariable("id").toUUID()
        val result = comments.findByPostId(id)
        return ok().body(result)
    }
}