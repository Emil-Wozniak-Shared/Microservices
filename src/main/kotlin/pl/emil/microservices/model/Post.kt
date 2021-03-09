package pl.emil.microservices.model

import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(value = "posts")
data class Post(
    @Id
    @Column("id")
    var id: UUID? = null,

    @Column("title")
    var title: String? = null,

    @Column("content")
    var content: String? = null,

    @Column("metadata")
    var metadata: Json? = null,

    @Column("status")
    var status: Status? = Status.DRAFT,

    @Column("created_at")
    @CreatedDate
    var createdAt: LocalDateTime? = null,

    @Column("updated_at")
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,

    @Column("version")
    @Version
    var version: Long? = null


) {
    enum class Status {
        DRAFT, PENDING_MODERATION, PUBLISHED
    }
}