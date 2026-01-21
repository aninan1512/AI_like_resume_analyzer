package com.example.resumeanalyzer.rules

import com.example.resumeanalyzer.AnalysisContext
import com.example.resumeanalyzer.TextUtils
import kotlin.math.roundToInt

class KeywordMatchRule : Rule {
    override fun evaluate(ctx: AnalysisContext): List<Finding> {
        if (ctx.jobKeywords.isEmpty()) {
            return listOf(
                Finding(
                    title = "No job description provided",
                    message = "Keyword match scoring is more accurate when you provide a job description.",
                    suggestion = "Run again with --job <path> to get keyword match insights.",
                    severity = Severity.INFO
                )
            )
        }

        val matches = TextUtils.countMatches(ctx.resume, ctx.jobKeywords)
        val ratio = matches.toDouble() / ctx.jobKeywords.size.toDouble()
        val percent = (ratio * 100).roundToInt()

        return when {
            ratio >= 0.60 -> listOf(
                Finding(
                    title = "Strong keyword alignment",
                    message = "Your resume matches about $percent% of top job keywords.",
                    severity = Severity.INFO,
                    bonus = 6
                )
            )
            ratio >= 0.35 -> listOf(
                Finding(
                    title = "Moderate keyword alignment",
                    message = "Your resume matches about $percent% of top job keywords.",
                    suggestion = "Add missing keywords naturally in Skills and project bullets (only if true).",
                    severity = Severity.MEDIUM,
                    penalty = 5
                )
            )
            else -> listOf(
                Finding(
                    title = "Low keyword alignment",
                    message = "Your resume matches about $percent% of top job keywords.",
                    suggestion = "Mirror key terms from the job post in Skills/Experience/Projects (truthfully).",
                    severity = Severity.HIGH,
                    penalty = 12
                )
            )
        }
    }
}
