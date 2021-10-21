package pl.emil.pdf

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import pl.emil.pdf.config.thenLog

@SpringBootApplication
class PdfApplication

fun main(args: Array<String>) {
    runApplication<PdfApplication>(*args).thenLog()
}
