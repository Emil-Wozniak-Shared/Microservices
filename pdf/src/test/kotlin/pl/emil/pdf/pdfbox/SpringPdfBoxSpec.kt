package pl.emil.pdf.pdfbox

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import pl.emil.pdf.helpers.DocumentHelper
import pl.emil.pdf.helpers.WebHelper
import pl.emil.pdf.helpers.hasFonts
import pl.emil.pdf.helpers.writeByteArrayToFile
import java.io.File
import java.nio.file.InvalidPathException

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "50000")
class SpringPdfBoxSpec(private val client: WebTestClient) : FeatureSpec({
    lateinit var file: File
    lateinit var doc: PDDocument
    val dir = tempdir()
    val stripper = PDFTextStripper()
    feature("file test") {
        scenario("file exists") {
            file.exists() shouldBe true
        }
        scenario("should have ") {
            file.isFile shouldBe true
        }
        scenario("pdf name") {
            file.name shouldBe "response.pdf"
        }
        scenario("should contain header 1") {
            stripper.getText(doc).contains("Thank you for shopping at our company.") shouldBe true
        }
        scenario("should contain header 2") {
            stripper.getText(doc).contains("Below you can find the attached details.") shouldBe true
        }
        scenario("should contain div with text") {
            stripper.getText(doc)
                .contains("Adobe Reader is required to view PDF files. This is a free") shouldBe true
        }
        scenario("should contain a href name value") {
            stripper.getText(doc).contains("Adobe website") shouldBe true
        }
        scenario("should contain fonts: Times-Bold, Times-Roman and Helvetica  ") {
            doc.pages.hasFonts("Times-Bold", "Times-Roman", "Helvetica") shouldBe true
        }

    }
    afterSpec {
        dir.delete()
    }
    beforeSpec {
        try {
            client.get()
                .uri("/api/pdf/fly")
                .exchange()
                .returnResult<Any>()
                .let { response ->
                    response.status shouldBe OK
                    response.responseBodyContent shouldNotBe null
                    file = File("${dir.path}${File.separator}response.pdf")
                        .writeByteArrayToFile(response.responseBodyContent)
                        .apply {
                            parentFile.mkdirs()
                            createNewFile()
                        }
                    doc = PDDocument.load(file)
                }
        } catch (e: InvalidPathException) {
            println("error creating temporary test file in ${this.javaClass.simpleName}")
        }
    }
}), WebHelper, DocumentHelper

