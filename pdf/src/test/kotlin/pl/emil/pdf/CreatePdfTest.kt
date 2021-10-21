package pl.emil.pdf

import com.codeborne.pdftest.PDF
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import org.springframework.core.io.ClassPathResource
import pl.emil.pdf.handler.hasText
import java.io.File

class CreatePdfTest : FeatureSpec({
    feature("Read PDF content") {
        val path = "src/test/resources/quickideas.pdf"
        val resource = ClassPathResource("quickideas.pdf")
        val expected = "50 Quick Ideas"
        val file = File(path)
        scenario("from file should  contains '50 Quick Ideas'") {
            PDF(file) hasText expected shouldBe true
        }
        scenario("from url should  contains '50 Quick Ideas'") {
            PDF(resource.url) hasText expected shouldBe true
        }
        scenario("from uri should  contains '50 Quick Ideas'") {
            PDF(resource.file.toURI()) hasText expected shouldBe true
        }
        scenario("from InputStream should  contains '50 Quick Ideas'") {
            PDF(resource.inputStream) hasText expected shouldBe true
        }
        scenario("from file with page limit should  contains '50 Quick Ideas'") {
            PDF(file, 1, 2) hasText expected shouldBe true
        }
        scenario("from url file with page limit should  contains '50 Quick Ideas'") {
            PDF(resource.url, 1, 2) hasText expected shouldBe true
        }
        scenario("from uri with page limit should  contains '50 Quick Ideas'") {
            PDF(resource.file.toURI(), 1, 2) hasText expected shouldBe true
        }
        scenario("from input stream with page limit should  contains '50 Quick Ideas'") {
            PDF(resource.inputStream, 1, 2) hasText expected shouldBe true
        }
    }
})
