package com.example.cricscore.view.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

fun createSummaryPdf(context: Context, text: String): File {
    val doc   = PdfDocument()
    val pageW = 300  // points (~4″ wide) – good for share; adjust if needed
    val pageH = 600

    val pageInfo = PdfDocument.PageInfo.Builder(pageW, pageH, 1).create()
    val page     = doc.startPage(pageInfo)
    val canvas: Canvas = page.canvas

    val paint = Paint().apply {
        textSize = 12f
    }

    // Split long text into lines that fit page width (simple wrap)
    val maxChars = 45
    var y = 20
    text.lines().forEach { raw ->
        var line = raw
        while (line.length > maxChars) {
            canvas.drawText(line.take(maxChars), 10f, y.toFloat(), paint)
            line = line.drop(maxChars)
            y += 18
        }
        canvas.drawText(line, 10f, y.toFloat(), paint)
        y += 18
    }

    doc.finishPage(page)

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val file = File(context.getExternalFilesDir(null), "match_summary_$timeStamp.pdf")

    FileOutputStream(file).use { doc.writeTo(it) }
    doc.close()
    return file
}
