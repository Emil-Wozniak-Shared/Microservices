package pl.emil.pdf.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_PDF
import org.springframework.web.reactive.function.server.router
import pl.emil.pdf.utils.PDFCreator

@Configuration
class RouteConfig {
    @Bean
    fun routes(pdfService: PDFCreator) = router {
        "api".nest {
            "pdf".nest {
                val filename = "output.pdf"
                GET("sample") {
                    ok()
                        .headers {
                            HttpHeaders().apply {
                                contentType = APPLICATION_PDF
                                setContentDispositionFormData(filename, filename)
                                cacheControl = "must-revalidate, post-check=0, pre-check=0"
                            }
                        }
                        .bodyValue(pdfService.create())
                }
            }
        }
    }
}
