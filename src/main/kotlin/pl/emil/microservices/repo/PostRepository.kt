package pl.emil.microservices.repo

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.emil.microservices.model.Post
import java.util.*

interface PostRepository : ReactiveCrudRepository<Post, UUID>