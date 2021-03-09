package pl.emil.microservices.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "comments")
data class Comment(
    @Id
    @Column(name = "id")
    var id: UUID? = UUID.randomUUID(),

    @Column(name = "content")
    var content: String? = null,

    @Column(name = "post_id")
    var postId: UUID? = null,

    @Column(name = "created_at")
    @CreatedDate
    var createdAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,

    @Column(name = "version")
    @Version
    var version: Long? = null
)