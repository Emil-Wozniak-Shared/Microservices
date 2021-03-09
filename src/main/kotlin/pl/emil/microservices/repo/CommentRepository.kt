package pl.emil.microservices.repo

import org.springframework.data.r2dbc.repository.R2dbcRepository
import pl.emil.microservices.model.Comment
import reactor.core.publisher.Flux
import java.util.*

interface CommentRepository : R2dbcRepository<Comment, UUID> {
    fun findByPostId(postId: UUID): Flux<Comment>
}