package pl.emil.posts.repo

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.emil.posts.model.Post
import java.util.*

interface PostRepository : ReactiveCrudRepository<Post, UUID>