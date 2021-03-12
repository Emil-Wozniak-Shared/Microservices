package pl.emil.posts.web.handlers

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import pl.emil.posts.model.Comment
import pl.emil.posts.repo.CommentRepository
import reactor.core.publisher.Mono
import java.net.URI

@Component
class CommentHandler(private val comments: CommentRepository) : ApiHandler<Comment> {
    override fun create(request: ServerRequest): Mono<ServerResponse> =
        request
            .bodyToMono(Comment::class.java).flatMap { comment ->
                this.comments.save(comment).flatMap {
                    created(URI.create("/posts/${request.id()}/comments/${it.id}")).build()
                }
            }
            .doOnError { throw Exception(it) }

    override fun getOne(request: ServerRequest): Mono<ServerResponse> =
        ok()
            .body(comments.findByPostId(request.id()))
            .doOnError { throw Exception(it) }

    override fun all(request: ServerRequest): Mono<ServerResponse> {
        TODO("Not yet implemented")
    }

    override fun update(request: ServerRequest): Mono<ServerResponse> {
        TODO("Not yet implemented")
    }

    override fun delete(request: ServerRequest): Mono<ServerResponse> {
        TODO("Not yet implemented")
    }
}