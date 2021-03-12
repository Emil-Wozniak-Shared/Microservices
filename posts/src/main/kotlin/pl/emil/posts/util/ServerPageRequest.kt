package pl.emil.posts.util

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.ASC
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import reactor.core.publisher.Mono

private fun asPageable(request: ServerRequest): Pageable {
    val params = request.queryParams()
    if (params.keys.size > 2) {
        val page = params.getFirst("page") ?: "0"
        val size = params.getFirst("size") ?: "10"
        val sort = params.getFirst("sort") ?: "id,asc"
        return sortablePage(sort, page.toInt(), size.toInt()) ?: PageRequest.of(page.toInt(), size.toInt())
    }
    return Pageable.unpaged()
}

/**
 * experimental feature
 *
 * @param sortValue ServerRequest sort param value
 * @param page      page number
 * @param size      page size
 * @return Pageable
 */
private fun sortablePage(sortValue: String, page: Int, size: Int): PageRequest? {
    if (isSortable(sortValue)) {
        val parameters = sortValue
            .replace("asc", " ")
            .replace("desc", " ")
            .replace(", ".toRegex(), "")
            .replace(" ,".toRegex(), "")
        val sort = if (isSortDirection.invoke(sortValue)) Sort.by(
            if (isAscending.invoke(sortValue)) ASC
            else DESC,
            parameters
        ) else Sort.by(*sortValue.split(",").toTypedArray())
        return PageRequest.of(page, size, sort)
    }
    return null
}

private fun isSortable(sortValue: String): Boolean {
    return sortValue.isNotEmpty() || sortValue.isNotBlank()
}

fun pageRequest(
    request: ServerRequest,
    callback: (Pageable) -> Mono<ServerResponse>
): Mono<ServerResponse> {
    val pageable = asPageable(request)
    return if (pageable.isPaged) callback.invoke(pageable)
    else badRequest().build()
}

private val isSortDirection: (String) -> Boolean = { it ->
    it.contains("asc") ||
            it.contains("desc")
}
private val isAscending: (String) -> Boolean = { sortValue ->
    sortValue.contains("asc")
}
