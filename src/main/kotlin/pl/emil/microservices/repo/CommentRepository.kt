package pl.emil.microservices.repo

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.emil.microservices.model.Comment
import reactor.core.publisher.Flux
import java.util.*

interface CommentRepository : ReactiveCrudRepository<Comment, UUID> {
    fun findByPostId(postId: UUID): Flux<Comment>
}