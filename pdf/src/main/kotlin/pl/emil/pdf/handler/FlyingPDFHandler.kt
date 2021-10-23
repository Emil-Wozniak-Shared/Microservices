package pl.emil.pdf.handler

import com.lowagie.text.DocumentException
import com.lowagie.text.pdf.PdfName
import com.lowagie.text.pdf.PdfObject
import com.lowagie.text.pdf.PdfString
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.htmlcleaner.CleanerProperties
import org.htmlcleaner.DomSerializer
import org.htmlcleaner.HtmlCleaner
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.w3c.dom.Document
import org.xhtmlrenderer.pdf.DefaultPDFCreationListener
import org.xhtmlrenderer.pdf.ITextRenderer
import pl.emil.contract.model.User
import pl.emil.pdf.templates.MailBodyTemplate
import pl.emil.pdf.web.UserClient
import pl.emil.pdf.web.bodyFlowAndAwait
import pl.emil.pdf.web.pdf
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.xml.parsers.ParserConfigurationException

@Component
class FlyingPDFHandler(private val userClient: UserClient) : PDFCreator {

    var models: HashMap<String, Any> = hashMapOf()

    private fun getTemplate() = MailBodyTemplate.createTemplate(models)

    suspend fun getPdf(request: ServerRequest): ServerResponse =
        try {
            userClient.getUsers {
                models.put("users", it)
            }
            getModels(models)
            val os = ByteArrayOutputStream()
            ITextRenderer().apply {
                setDocument(prepareDocument().await(), null)
                layout()
                listener = MetaDataCreationListener()
                createPDF(os)
            }
            ok().pdf("flying.pdf").bodyFlowAndAwait(os.toByteArray())
        } catch (documentException: DocumentException) {
            documentException.printStackTrace()
            badRequest().bodyFlowAndAwait(ByteArray(0))
        }

    @Throws(IOException::class)
    suspend fun prepareDocument(): Deferred<Document> = GlobalScope.async {
        try {
            DomSerializer(CleanerProperties()).createDOM(HtmlCleaner().clean(getTemplate()))
        } catch (e: ParserConfigurationException) {
            throw IllegalStateException("Could not create document from html")
        }
    }

    internal class MetaDataCreationListener : DefaultPDFCreationListener() {
        override fun preOpen(renderer: ITextRenderer) {
            renderer.writer.info.apply {
                put(PdfName("Author"), PdfString("Developer", PdfObject.TEXT_UNICODE))
                put(PdfName("Producer"), PdfString("DonRiver Inc.", PdfObject.TEXT_UNICODE))
            }
        }
    }
}


