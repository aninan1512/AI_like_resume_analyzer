package com.example.resumeanalyzer.rules

import com.example.resumeanalyzer.AnalysisContext
import com.example.resumeanalyzer.TextUtils

class MetricsRule : Rule {
    private val metricRegex = Regex("""(\b\d{1,3}%\b)|(\b\d{1,3}\+?\b)|(\b\d{4}\b)|(\b\d+(\.\d+)?\b)""")

    override fun evaluate(ctx: AnalysisContext): List<Finding> {
        val lines = TextUtils.lines(ctx.resumeRaw)
        val bulletLines = lines.filter { it.trim().startsWith("-") || it.trim().startsWith("•") || it.trim().startsWith("*") }

        if (bulletLines.isEmpty()) {
            return listOf(
                Finding(
                    title = "Few or no bullet points detected",
                    message = "Bullet points make impact and responsibilities easier to scan.",
                    suggestion = "Use bullet points under each role/project (2–6 bullets each).",
                    severity = Severity.MEDIUM,
                    penalty = 6
                )
            )
        }

        val bulletsWithMetrics = bulletLines.count { metricRegex.containsMatchIn(it) }
        val ratio = bulletsWithMetrics.toDouble() / bulletLines.size.toDouble()

        return when {
            ratio >= 0.45 -> listOf(
                Finding(
                    title = "Good use of measurable impact",
                    message = "Many bullet points include numbers or measurable outcomes.",
                    severity = Severity.INFO,
                    bonus = 4
                )
            )
            ratio >= 0.20 -> listOf(
                Finding(
                    title = "Some measurable impact present",
                    message = "A few bullets include numbers, but you can add more metrics.",
                    suggestion = "Add results like time saved, % improvement, volume handled, users impacted, etc.",
                    severity = Severity.LOW,
                    penalty = 3
                )
            )
            else -> listOf(
                Finding(
                    title = "Low measurable impact",
                    message = "Most bullet points lack metrics (numbers, percentages, scale).",
                    suggestion = "Add at least 1–2 metrics per role/project (e.g., reduced errors by 20%, served 60+ customers/day).",
                    severity = Severity.HIGH,
                    penalty = 10
                )
            )
        }
    }
}
