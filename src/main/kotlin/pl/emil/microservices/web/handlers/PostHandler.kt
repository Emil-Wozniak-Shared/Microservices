package pl.emil.microservices.web.handlers

import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.*
import pl.emil.microservices.model.Post
import pl.emil.microservices.repo.PostRepository
import java.net.URI
import java.time.LocalDateTime

@Component
class PostHandler(private val posts: PostRepository) : ApiHandler<Post> {
    override fun all(request: ServerRequest): ServerResponse =
        ok().body(this.posts.findAll())

    override fun getOne(request: ServerRequest): ServerResponse =
        this.posts
            .findById(request.id())
            .map { ok().contentType(request.mediaType()).body(it) }
            .orElseGet { notFound().build() }

    override fun create(request: ServerRequest): ServerResponse =
        request
            .body(Post::class.java).let { post ->
                post.createdAt = LocalDateTime.now()
                this.posts.save(post)
                created(URI.create("/posts/" + post.id)).build()
            }

    override fun update(request: ServerRequest): ServerResponse {
        this.posts.findById(request.id())
            .ifPresent {
                val p2 = request.body(Post::class.java)
                it.title = p2.title
                it.content = p2.content
                it.metadata = p2.metadata
                it.status = p2.status
                this.posts.save(it)
            }
        return noContent().build()
    }


    override fun delete(request: ServerRequest): ServerResponse =
        this.posts.findById(request.id())
            .map {
                this.posts.delete(it)
                noContent().build()
            }.orElseGet { badRequest().build() }

}