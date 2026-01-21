package com.example.resumeanalyzer

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileInputStream

object DocumentTextExtractor {

    fun extractText(file: File): String {
        val name = file.name.lowercase()

        return when {
            name.endsWith(".txt") -> file.readText()

            name.endsWith(".docx") -> extractDocx(file)

            name.endsWith(".pdf") -> extractPdf(file)

            else -> throw IllegalArgumentException("Unsupported file type: ${file.name}")
        }
    }

    private fun extractDocx(file: File): String {
        FileInputStream(file).use { fis ->
            val doc = XWPFDocument(fis)
            // Extract paragraphs + tables as plain text
            val sb = StringBuilder()

            doc.paragraphs.forEach { p ->
                val t = p.text?.trim().orEmpty()
                if (t.isNotBlank()) sb.appendLine(t)
            }

            doc.tables.forEach { table ->
                table.rows.forEach { row ->
                    row.tableCells.forEach { cell ->
                        val t = cell.text?.trim().orEmpty()
                        if (t.isNotBlank()) sb.appendLine(t)
                    }
                }
            }

            return sb.toString().trim()
        }
    }

    private fun extractPdf(file: File): String {
        PDDocument.load(file).use { pdf ->
            val stripper = PDFTextStripper()
            return stripper.getText(pdf).trim()
        }
    }
}
