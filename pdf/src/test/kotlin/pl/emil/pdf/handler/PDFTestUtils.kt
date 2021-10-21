package pl.emil.pdf.handler

import com.codeborne.pdftest.PDF

infix fun PDF.hasText(text: String) = PDF.containsText(text).matches(this)
