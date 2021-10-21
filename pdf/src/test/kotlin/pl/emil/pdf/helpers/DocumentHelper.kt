package pl.emil.pdf.helpers

import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.cos.COSObject
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageTree

interface DocumentHelper

fun PDPageTree.hasFonts(vararg fonts: String) = this
    .flatMap { (it as PDPage).cosObject.values }
    .filterIsInstance<COSDictionary>()
    .flatMap { it.values }
    .filterIsInstance<COSDictionary>()
    .flatMap { it.values }
    .map { (it as COSObject).`object` }
    .flatMap { (it as COSDictionary).values }
    .map { (it as COSName).name }
    .any { fonts.any { font -> font == it } }
