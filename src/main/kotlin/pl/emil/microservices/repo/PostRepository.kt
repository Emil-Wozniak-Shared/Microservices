package pl.emil.microservices.repo

import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import pl.emil.microservices.model.Post
import pl.emil.microservices.model.PostSummary
import reactor.core.publisher.Flux
import java.util.*

interface PostRepository : R2dbcRepository<Post, UUID> {
    @Query("SELECT * FROM posts where title like :title")
    fun findByTitleContains(title: String): Flux<Post>
    fun findByTitleLike(title: String, pageable: Pageable): Flux<PostSummary>
}