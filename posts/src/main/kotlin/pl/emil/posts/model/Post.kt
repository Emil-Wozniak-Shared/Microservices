package pl.emil.posts.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotNull

@Table(value = "posts")
data class Post(
    @Id
    @Column(value = "id")
    var id: UUID? = UUID.randomUUID(),

    @NotNull(message = "Title could not be null")
    @Column(value = "title")
    var title: String? = null,

    @Column(value = "content")
    var content: String? = null,

    @Column(value = "metadata")
    var metadata: String? = null,

    @Column(value = "status")
    var status: Status? = null,

    @CreatedDate
    @Column(value = "created_at")
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(value = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @Version
    @Column(value = "version")
    var version: Long? = null
) {
    enum class Status {
        DRAFT,
        PENDING_MODERATION,
        PUBLISHED
    }
}