package pl.emil.microservices.web.handlers

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.ServerResponse.ok
import pl.emil.microservices.model.Comment
import pl.emil.microservices.repo.CommentRepository
import reactor.core.publisher.Mono
import java.net.URI
import java.util.*


@Component
class CommentHandler(private val comments: CommentRepository) {
    fun create(req: ServerRequest): Mono<ServerResponse> {
        val postId = UUID.fromString(req.pathVariable("id"))
        return req.bodyToMono(Comment::class.java)
            .map { comment ->
                comment.postId = postId
                comment
            }
            .flatMap(comments::save)
            .flatMap { c -> created(URI.create("/posts/" + postId + "/comments/" + c.id)).build() }
    }

    fun getByPostId(req: ServerRequest): Mono<ServerResponse> {
        val id = req.pathVariable("id").toUUID()
        val result = comments.findByPostId(id)
        return ok().body(result, Comment::class.java)
    }

}