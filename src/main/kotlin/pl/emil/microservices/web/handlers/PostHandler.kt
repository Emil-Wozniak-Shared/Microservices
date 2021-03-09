package pl.emil.microservices.web.handlers

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import pl.emil.microservices.model.Post
import pl.emil.microservices.repo.PostRepository
import reactor.core.publisher.Mono
import java.net.URI

@Component
class PostHandler(private val posts: PostRepository) : ApiHandler<Post> {
    override fun all(request: ServerRequest): Mono<ServerResponse> =
        ok().body(this.posts.findAll(), Post::class.java)

    override fun getOne(request: ServerRequest): Mono<ServerResponse> =
        this.posts
            .findById(request.id())
            .flatMap { ok().contentType(request.mediaType()).bodyValue(it) }
            .switchIfEmpty(notFound().build())

    override fun create(request: ServerRequest): Mono<ServerResponse> =
        request
            .bodyToMono(Post::class.java)
            .flatMap(this.posts::save)
            .flatMap { post -> created(URI.create("/posts/" + post.id)).build() }

    override fun update(request: ServerRequest): Mono<ServerResponse> = Mono
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

    override fun delete(request: ServerRequest): Mono<ServerResponse> =
        this.posts.deleteById(request.id()).flatMap { noContent().build() }

}