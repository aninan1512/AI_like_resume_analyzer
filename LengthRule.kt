package com.example.resumeanalyzer.rules

import com.example.resumeanalyzer.AnalysisContext
import com.example.resumeanalyzer.TextUtils

class LengthRule : Rule {
    override fun evaluate(ctx: AnalysisContext): List<Finding> {
        val wordCount = TextUtils.countWords(ctx.resumeRaw)

        // Not perfect, but a decent heuristic for text resumes.
        return when {
            wordCount < 220 -> listOf(
                Finding(
                    title = "Resume may be too short",
                    message = "Estimated word count is $wordCount, which may not show enough detail.",
                    suggestion = "Add more project details, tools, and measurable outcomes (without fluff).",
                    severity = Severity.MEDIUM,
                    penalty = 6
                )
            )
            wordCount in 220..900 -> listOf(
                Finding(
                    title = "Resume length looks reasonable",
                    message = "Estimated word count is $wordCount.",
                    severity = Severity.INFO,
                    bonus = 2
                )
            )
            wordCount in 901..1200 -> listOf(
                Finding(
                    title = "Resume may be long",
                    message = "Estimated word count is $wordCount, which might be dense for ATS scanning.",
                    suggestion = "Tighten bullets, remove repetition, and prioritize the most relevant content.",
                    severity = Severity.LOW,
                    penalty = 3
                )
            )
            else -> listOf(
                Finding(
                    title = "Resume likely too long",
                    message = "Estimated word count is $wordCount, which may hurt readability and ATS clarity.",
                    suggestion = "Aim for 1 page (early career) or 2 pages max. Reduce older/less relevant content.",
                    severity = Severity.HIGH,
                    penalty = 10
                )
            )
        }
    }
}
