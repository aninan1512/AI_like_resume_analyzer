package com.example.resumeanalyzer

import com.example.resumeanalyzer.rules.Finding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Report(
    val score: Int,
    val summary: String,
    val findings: List<Finding>,
    val jobKeywords: List<String>
) {
    fun toConsoleString(): String {
        val ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val sb = StringBuilder()

        sb.appendLine("=== Resume Analyzer Report ===")
        sb.appendLine("Generated: $ts")
        sb.appendLine("Score: $score/100")
        sb.appendLine("Summary: $summary")
        sb.appendLine()

        if (jobKeywords.isNotEmpty()) {
            sb.appendLine("Top job keywords detected (${jobKeywords.size}):")
            sb.appendLine(jobKeywords.joinToString(", "))
            sb.appendLine()
        }

        if (findings.isEmpty()) {
            sb.appendLine("No issues detected. Nice work.")
            return sb.toString()
        }

        sb.appendLine("Findings:")
        findings.forEachIndexed { idx, f ->
            sb.appendLine("${idx + 1}. [${f.severity}] ${f.title}")
            sb.appendLine("   - ${f.message}")
            if (f.suggestion.isNotBlank()) sb.appendLine("   - Suggestion: ${f.suggestion}")
        }

        return sb.toString()
    }
}
