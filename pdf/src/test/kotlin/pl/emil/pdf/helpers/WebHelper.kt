package pl.emil.pdf.helpers

import org.apache.commons.io.FileUtils
import java.io.File

interface WebHelper

fun File.writeByteArrayToFile(data: ByteArray?): File = this.apply {
    FileUtils.writeByteArrayToFile(this, data)
}
