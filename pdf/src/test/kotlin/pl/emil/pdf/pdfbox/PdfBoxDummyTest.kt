package pl.emil.pdf.pdfbox

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDDocument.load
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.core.io.ClassPathResource
import java.io.File

class PdfBoxDummyTest: FeatureSpec({
    lateinit var pdf: File
    lateinit var doc: PDDocument
    val stripper = PDFTextStripper()
    beforeTest {  }
    beforeAny {
        pdf = ClassPathResource("example.pdf").file
        doc = load(pdf)
    }
    feature("test by pdfbox stripper") {
        scenario("should have 30 pages") {
            doc.numberOfPages shouldBe 30
        }
        scenario("should have ") {
            pdf.isFile shouldBe true
        }
        scenario("pdf name") {
            pdf.name shouldBe "example.pdf"
        }
        scenario("strip text") {
            stripper.getText(doc).contains("Pâte brisée") shouldBe true
        }
    }
})
