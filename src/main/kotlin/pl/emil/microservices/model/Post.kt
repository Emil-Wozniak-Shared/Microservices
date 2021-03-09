package pl.emil.microservices.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "posts")
data class Post(
    @Id
    @Column(name = "id")
    var id: UUID? = UUID.randomUUID(),

    @NotNull(message = "Title could not be null")
    @Column(name = "title")
    var title: String? = null,

    @Column(name = "content")
    var content: String? = null,

    @Column(name = "metadata")
    var metadata: String? = null,

    @Column(name = "status")
    var status: Status? = null,

    @CreatedDate
    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @Version
    @Column(name = "version")
    var version: Long? = null

) : Serializable {
    enum class Status {
        DRAFT, PENDING_MODERATION, PUBLISHED
    }
}