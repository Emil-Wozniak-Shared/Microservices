package pl.emil.pdf.web

import kotlinx.coroutines.flow.asFlow
import org.springframework.http.CacheControl
import org.springframework.http.ContentDisposition
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait

fun ServerResponse.BodyBuilder.pdf(filename: String) =
    this
        .headers {
            it.setCacheControl(CacheControl.noCache())
            it.contentType = MediaType.APPLICATION_PDF
            it.contentDisposition = ContentDisposition.builder("inline")
                .filename("$filename.pdf")
                .build()
        }

suspend inline fun <reified T : Any> ServerResponse.BodyBuilder.bodyFlowAndAwait(body: T) =
    this.bodyAndAwait(listOf(body).asFlow())
