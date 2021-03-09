package pl.emil.microservices.model

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
    @Column("id")
    var id: UUID? = null,

    @Column("content")
    var content: String? = null,

    @Column("post_id")
    var postId: UUID? = null,

    @Column("created_at")
    @CreatedDate
    var createdAt: LocalDateTime? = null,

    @Column("updated_at")
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,

    @Column("version")
    @Version
    var version: Long? = null
)