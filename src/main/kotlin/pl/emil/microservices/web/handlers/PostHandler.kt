package pl.emil.microservices.web.handlers

import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import pl.emil.microservices.config.exception.ExceptionResponse
import pl.emil.microservices.model.Post
import pl.emil.microservices.repo.PostRepository
import pl.emil.microservices.web.validator.RequestValidator
import reactor.core.publisher.Mono
import java.net.URI
import java.time.LocalDateTime.now

@Component
class PostHandler(
    private val posts: PostRepository,
    private val validator: RequestValidator
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
                post.createdAt = now()
                this.posts.save(post).subscribe()
            }
            .flatMap {
                created(URI.create("/posts/${it.id}")).build()
            }
//            .onErrorResponse()

    @Transactional
    override fun update(request: ServerRequest): Mono<ServerResponse> =
        Mono
            .zip(
                { data: Array<Any> ->
                    val p: Post = data[0] as Post
                    val p2: Post = data[1] as Post
                    p.title = p2.title
                    p.content = p2.content
                    p.metadata = p2.metadata
                    p.status = p2.status
                    p
                },
                this.posts.findById(request.id()),
                request.bodyToMono(Post::class.java)
            )
            .cast(Post::class.java)
            .flatMap<Any>(this.posts::save)
            .flatMap { noContent().build() }
            .onErrorResume {
                val response = ExceptionResponse(it.message, it.toString())
                badRequest().body(Mono.just(response))
            }

    override fun delete(request: ServerRequest): Mono<ServerResponse> =
        this.posts
            .deleteById(request.id())
            .flatMap { noContent().build() }
            .onErrorResume {
                badRequest().body<String>(Mono.just(it.message!!))
            }

}