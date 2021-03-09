package pl.emil.microservices.repo

import org.springframework.data.jpa.repository.JpaRepository
import pl.emil.microservices.model.Comment
import reactor.core.publisher.Flux
import java.util.*

interface CommentRepository : JpaRepository<Comment, UUID> {
    fun findByPostId(postId: UUID): List<Comment>
}