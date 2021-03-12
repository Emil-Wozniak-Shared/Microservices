package pl.emil.posts.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(value = "comments")
data class Comment(
    @Id
    @Column(value = "id")
    var id: UUID? = UUID.randomUUID(),

    @Column(value = "content")
    var content: String? = null,

    @Column(value = "post_id")
    var postId: UUID? = null,

    @Column(value = "created_at")
    @CreatedDate
    var createdAt: LocalDateTime? = null,

    @Column(value = "updated_at")
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,

    @Column(value = "version")
    @Version
    var version: Long? = null
)