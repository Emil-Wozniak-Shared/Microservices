package pl.emil.pdf.utils

import org.apache.fop.apps.Fop
import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants.MIME_PDF
import org.htmlcleaner.CleanerProperties
import org.htmlcleaner.DomSerializer
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.TagNode
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.w3c.dom.Document
import pl.emil.pdf.templates.MailBodyTemplate
import java.io.*
import java.net.URI
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.*
import javax.xml.transform.dom.DOMResult
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.sax.SAXResult
import kotlin.system.exitProcess

@Component
class SamplePDFCreator : PDFCreator() {
    override fun create(): ByteArray = createLines()

    val models = hashMapOf<String, Any>()

    fun createLines(): ByteArray {
        val bytes = fo2Pdf(getXslFo(getXml()), ClassPathResource("xhtml2fo.xsl").inputStream)
        FileOutputStream(ClassPathResource("blank.pdf").file).apply {
            write(bytes)
            close()
        }
        return bytes
    }

    @Throws(IOException::class, ParserConfigurationException::class)
    private fun getXml(): Document {
        val resource = ClassPathResource("names.txt")
        resource.file.readLines().let { names ->
            models.put("objects", names)
        }
//        val file: InputStream = ClassPathResource("mailBody.html").inputStream
        val input: InputStream = MailBodyTemplate.createTemplate(models).byteInputStream()
        val cleaner = HtmlCleaner()
        val props: CleanerProperties = cleaner.properties
        val serializer = DomSerializer(props, true)
        val tag: TagNode = cleaner.clean(input)
        return serializer.createDOM(tag)
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

    private fun fo2Pdf(foDocument: Document?, styleSheet: InputStream): ByteArray =
        try {
            ByteArrayOutputStream().let { out ->
                transform(styleSheet, foDocument, out)
                out.toByteArray()
            }
        } catch (ex: Exception) {
            ByteArray(0)
        }

    private fun transform(styleSheet: InputStream, foDocument: Document?, out: ByteArrayOutputStream): Unit? {
        val factory = FopFactory.newInstance(URI(ClassPathResource("example.pdf").path))
        val fop: Fop = factory.newFop(MIME_PDF, out)
        val src: Source = DOMSource(foDocument)
        val res: Result = SAXResult(fop.defaultHandler)
        return getTransformer(styleSheet)?.transform(src, res)
    }


    private fun getTransformer(styleSheet: InputStream): Transformer? =
        try {
            val documentBuilderFactory = DocumentBuilderFactory.newInstance().apply {
                isNamespaceAware = true
            }
            val documentBuilder = documentBuilderFactory.newDocumentBuilder()
            val xslDoc = documentBuilder.parse(styleSheet)
            val xslDomSource = DOMSource(xslDoc)
            TransformerFactory.newInstance().newTransformer(xslDomSource)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
}
