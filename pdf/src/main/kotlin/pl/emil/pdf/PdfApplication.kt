package pl.emil.pdf

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PdfApplication

fun main(args: Array<String>) {
    runApplication<PdfApplication>(*args)
}
