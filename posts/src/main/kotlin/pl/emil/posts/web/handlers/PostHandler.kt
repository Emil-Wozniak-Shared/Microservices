package pl.emil.posts.web.handlers

import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import pl.emil.posts.config.exception.ExceptionResponse
import pl.emil.posts.model.Post
import pl.emil.posts.repo.PostRepository
import pl.emil.posts.web.validator.RequestValidator
import reactor.core.publisher.Mono
import java.net.URI
import java.time.LocalDateTime.now

@Component
class PostHandler(
    private val posts: PostRepository,
    private val validator: RequestValidator,
) : ApiHandler<Post> {
    override fun all(request: ServerRequest): Mono<ServerResponse> =
        ok().body(this.posts.findAll())
            .doOnError { throw Exception(it) }

    override fun getOne(request: ServerRequest): Mono<ServerResponse> =
        Mono.just(request)
            .flatMap {
                ok()
                    .contentType(request.mediaType())
                    .body(this.posts.findById(request.id()))
            }
            .onErrorResume {
                status(INTERNAL_SERVER_ERROR).body<String>(Mono.just(it.message!!))
            }

    @Transactional
    override fun create(request: ServerRequest): Mono<ServerResponse> =
        request
            .validateBody<Post>(validator, "title", "content")
            .doOnNext { post ->
                this.posts.save(post.apply { createdAt = now() }).subscribe()
            }
            .flatMap {
                created(URI.create("/posts/${it.id}")).build()
            }

    @Transactional
    override fun update(request: ServerRequest): Mono<ServerResponse> =
        Mono
            .zip(
                { data: Array<Any> ->
                    (data[0] as Post).let { post ->
                        val (_, title, content, metadata, status) = data[1] as Post
                        post.title = title
                        post.content = content
                        post.metadata = metadata
                        post.status = status
                    }
                },
                this.posts.findById(request.id()),
                request.bodyToMono(Post::class.java)
            )
            .cast(Post::class.java)
            .flatMap<Any>(this.posts::save)
            .flatMap { noContent().build() }
            .onErrorResume {
                badRequest().body(createExceptionResponse(it))
            }

    override fun delete(request: ServerRequest): Mono<ServerResponse> =
        this.posts
            .deleteById(request.id())
            .flatMap { noContent().build() }
            .onErrorResume {
                badRequest().body<String>(Mono.just(it.message!!))
            }

    private fun createExceptionResponse(it: Throwable): Mono<ExceptionResponse> {
        return Mono.just(ExceptionResponse(it.message, it.toString()))
    }
}
