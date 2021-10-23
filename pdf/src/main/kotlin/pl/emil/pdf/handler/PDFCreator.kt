package pl.emil.pdf.handler

import org.springframework.core.io.ClassPathResource
import pl.emil.pdf.model.Name

interface PDFCreator {

    fun getModels(models: HashMap<String, Any>) {
        ClassPathResource("names.txt").file.readLines().let { names ->
            models.put("objects", names.map { it.split("\t") }.map {
                Name(it[0], it[1], it[2], it[3])
            })
        }
    }
}
