package com.example.resumeanalyzer

import java.util.Locale

object TextUtils {

    private val stopWords = setOf(
        "a","an","the","and","or","but","if","then","else","when","while","to","of","in","on","for","with",
        "by","from","as","at","is","are","was","were","be","been","being","it","this","that","these","those",
        "you","your","we","our","they","their","i","me","my","can","could","should","would","will","may","might",
        "not","no","yes","do","does","did","done","have","has","had","than","too","very"
    )

    fun normalize(text: String): String {
        return text
            .replace("\u00A0", " ")
            .replace(Regex("[\r\t]+"), " ")
            .replace(Regex(" +"), " ")
            .trim()
    }

    fun lower(text: String): String = text.lowercase(Locale.getDefault())

    fun lines(text: String): List<String> =
        text.replace("\r\n", "\n").replace("\r", "\n").split("\n")

    fun countWords(text: String): Int {
        return Regex("\\b[\\p{L}\\p{N}]+\\b")
            .findAll(text)
            .count()
    }

    fun containsAnyIgnoreCase(haystack: String, needles: List<String>): Boolean {
        val h = lower(haystack)
        return needles.any { lower(it) in h }
    }

    fun extractKeywords(text: String, topN: Int = 20): List<String> {
        val tokens = Regex("\\b[\\p{L}][\\p{L}\\p{N}\\-]{1,}\\b")
            .findAll(lower(text))
            .map { it.value.trim('-') }
            .filter { it.length >= 3 }
            .filter { it !in stopWords }
            .toList()

        val freq = tokens.groupingBy { it }.eachCount()
        return freq.entries
            .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key })
            .take(topN)
            .map { it.key }
    }

    fun countMatches(text: String, keywords: List<String>): Int {
        if (keywords.isEmpty()) return 0
        val h = lower(text)
        return keywords.count { k -> k in h }
    }
}
