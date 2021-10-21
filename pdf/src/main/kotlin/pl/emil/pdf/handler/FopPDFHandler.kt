package pl.emil.pdf.handler

import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants.MIME_PDF
import org.htmlcleaner.DomSerializer
import org.htmlcleaner.HtmlCleaner
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.w3c.dom.Document
import pl.emil.pdf.templates.MailBodyTemplate
import pl.emil.pdf.web.UserClient
import pl.emil.pdf.web.bodyFlowAndAwait
import pl.emil.pdf.web.pdf
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URI
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMResult
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.sax.SAXResult
import kotlin.system.exitProcess

@Component
class FopPDFHandler(private val userClient: UserClient) : PDFCreator {
    var models: HashMap<String, Any> = hashMapOf()

    suspend fun createLines(request: ServerRequest): ServerResponse {
        getModels(models)
        val bytes = fo2Pdf(
            document = (getXml()),
            styleSheet = ClassPathResource("xhtml2fo.xsl").inputStream
        ).apply bytes@{
            FileOutputStream(ClassPathResource("blank.pdf").file).apply {
                write(this@bytes)
                close()
            }
        }
        return ok().pdf("fop.pdf").bodyFlowAndAwait(bytes)
    }

    @Throws(IOException::class, ParserConfigurationException::class)
    private fun getXml(): Document {
        val cleaner = HtmlCleaner()
        return DomSerializer(cleaner.properties, true)
            .createDOM(cleaner.clean(getTemplate()))
    }

    private fun getTemplate() = MailBodyTemplate.createTemplate(models).byteInputStream()

    private fun fo2Pdf(document: Document?, styleSheet: InputStream): ByteArray =
        try {
            ByteArrayOutputStream().let { out ->
                transform(styleSheet, document, out)
                out.toByteArray()
            }
        } catch (ex: Exception) {
            ByteArray(0)
        }

    private fun transform(styleSheet: InputStream, document: Document?, out: ByteArrayOutputStream): Unit? =
        getTransformer(styleSheet)?.transform(
            DOMSource(document),
            SAXResult(getHandler(out))
        )

    private fun getHandler(out: ByteArrayOutputStream) =
        FopFactory.newInstance(URI(ClassPathResource("example.pdf").path))
            .newFop(MIME_PDF, out).defaultHandler

    private fun getTransformer(styleSheet: InputStream): Transformer? =
        try {
            TransformerFactory.newInstance().let { factory ->
                factory.newTransformer(
                    DOMSource(
                        DocumentBuilderFactory.newInstance()
                            .apply {
                                isNamespaceAware = true
                            }
                            .newDocumentBuilder()
                            .parse(styleSheet)
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


    @Throws(Exception::class)
    private fun getXslFo(xml: Document): Document? =
        DOMResult().let { domResult ->
            TransformerFactory
                .newInstance()
                .newTransformer()?.run {
                    try {
                        transform(DOMSource(xml), domResult)
                    } catch (e: TransformerException) {
                        return null
                    }
                } ?: run {
                println("Error creating transformer")
                exitProcess(1)
            }
            domResult.node as Document
        }
}
