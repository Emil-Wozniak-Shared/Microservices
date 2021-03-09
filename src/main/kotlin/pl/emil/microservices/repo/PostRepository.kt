package pl.emil.microservices.repo

import org.springframework.data.jpa.repository.JpaRepository
import pl.emil.microservices.model.Post
import java.util.*

interface PostRepository : JpaRepository<Post, UUID>