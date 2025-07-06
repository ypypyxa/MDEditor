package com.pivnoydevelopment.mdeditor.viewer.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.transition.R
import java.net.URL

class MarkdownParser(private val context: Context) {

    fun parse(markdown: String): List<View> {
        val views = mutableListOf<View>()
        val lines = markdown.lines()

        var inTable = false
        val tableRows = mutableListOf<List<String>>()

        for (line in lines) {

            val trimmed = line.trim()
            val imageRegex = "^!\\[(.*?)]\\((.*?)\\)$".toRegex()

            // Таблицы
            if (trimmed.startsWith("|") && trimmed.endsWith("|")) {
                if (trimmed.matches(Regex("^[|:\\-\\s]+$"))) {
                    continue
                }

                inTable = true
                val row = trimmed.substring(1, trimmed.length - 1)
                    .split("|").map { it.trim() }
                tableRows.add(row)
                continue
            } else if (inTable) {
                views.add(createTable(tableRows))
                inTable = false
                tableRows.clear()
            }

            // Заголовки
            when {
                trimmed.startsWith("###### ") ->
                    views.add(createTextView(trimmed.removePrefix("###### "), 16f, Typeface.BOLD))
                trimmed.startsWith("##### ") ->
                    views.add(createTextView(trimmed.removePrefix("##### "), 18f, Typeface.BOLD))
                trimmed.startsWith("#### ") ->
                    views.add(createTextView(trimmed.removePrefix("#### "), 20f, Typeface.BOLD))
                trimmed.startsWith("### ") ->
                    views.add(createTextView(trimmed.removePrefix("### "), 22f, Typeface.BOLD))
                trimmed.startsWith("## ") ->
                    views.add(createTextView(trimmed.removePrefix("## "), 24f, Typeface.BOLD))
                trimmed.startsWith("# ") ->
                    views.add(createTextView(trimmed.removePrefix("# "), 26f, Typeface.BOLD))
                //Картинки
                imageRegex.matches(trimmed) -> {
                    val match = imageRegex.find(trimmed)!!
                    val description = match.groupValues[1]
                    val url = match.groupValues[2]
                    views.add(createImageView(url, description))
                }
                trimmed.isNotEmpty() -> views.add(createFormattedTextView(trimmed))
            }
        }

        if (inTable) {
            views.add(createTable(tableRows))
        }

        return views
    }

    private fun getColorFromAttr(attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }

    private fun createTextView(text: String, sizeSp: Float, style: Int): TextView {
        return TextView(context).apply {
            this.text = text
            textSize = sizeSp
            setTypeface(null, style)
            setTextColor(getColorFromAttr(R.attr.colorPrimary))
            setPadding(1, 8, 1, 8)
        }
    }

    private fun createFormattedTextView(text: String): TextView {
        val spannable = SpannableStringBuilder(text)

        val regex = Regex(
            pattern = """
                (~~\*\*\*(.*?)\*\*\*~~) # перечёркнутый жирный курсив
                | (~~\*\*(.*?)\*\*~~) # перечёркнутый жирный
                | (~~\*(.*?)\*~~) # перечёркнутый курсив
                | (\*\*\*(.*?)\*\*\*) # жирный курсив
                | (\*\*(.*?)\*\*) # жирный
                | (\*(.*?)\*) # курсив
                | (~~(.*?)~~) # перечёркнутый
                """.trimIndent(),
            option = RegexOption.COMMENTS
        )

        val matches = regex.findAll(spannable).toList()

        for (match in matches.asReversed()) {
            val start = match.range.first
            val end = match.range.last + 1

            val (content, spans) = when {
                match.groupValues[2].isNotEmpty() -> match.groupValues[2] to listOf(StrikethroughSpan(), StyleSpan(Typeface.BOLD), StyleSpan(Typeface.ITALIC))
                match.groupValues[4].isNotEmpty() -> match.groupValues[4] to listOf(StrikethroughSpan(), StyleSpan(Typeface.BOLD))
                match.groupValues[6].isNotEmpty() -> match.groupValues[6] to listOf(StrikethroughSpan(), StyleSpan(Typeface.ITALIC))
                match.groupValues[8].isNotEmpty() -> match.groupValues[8] to listOf(StyleSpan(Typeface.BOLD), StyleSpan(Typeface.ITALIC))
                match.groupValues[10].isNotEmpty() -> match.groupValues[10] to listOf(StyleSpan(Typeface.BOLD))
                match.groupValues[12].isNotEmpty() -> match.groupValues[12] to listOf(StyleSpan(Typeface.ITALIC))
                match.groupValues[14].isNotEmpty() -> match.groupValues[14] to listOf(StrikethroughSpan())
                else -> continue
            }

            spannable.replace(start, end, content)

            spans.forEach { span ->
                spannable.setSpan(
                    span,
                    start,
                    start + content.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        return TextView(context).apply {
            this.text = spannable
            setTextColor(getColorFromAttr(R.attr.colorPrimary))
        }
    }

    private fun createImageView(url: String, description: String): ImageView {
        val imageView = ImageView(context)
        imageView.contentDescription = description
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        Thread {
            try {
                val bitmap = BitmapFactory.decodeStream(URL(url).openStream())
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
        return imageView
    }

    private fun createTable(rows: List<List<String>>): TableLayout {
        val tableLayout = TableLayout(context)
        tableLayout.setPadding(1, 8, 1, 8)

        val border = GradientDrawable().apply {
            setColor(Color.TRANSPARENT)
            setStroke(2, Color.GRAY)
        }

        rows.forEach { row ->
            val tableRow = TableRow(context)
            row.forEach { cell ->
                val textView = TextView(context).apply {
                    text = cell
                    setTextColor(getColorFromAttr(R.attr.colorPrimary))
                    setPadding(8, 8, 8, 8)
                    textSize = 14f
                    background = border
                }
                tableRow.addView(textView)
            }
            tableLayout.addView(tableRow)
        }
        return tableLayout
    }
}