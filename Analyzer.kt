package com.example.resumeanalyzer

import com.example.resumeanalyzer.rules.*
import kotlin.math.max
import kotlin.math.min

class Analyzer {
    private val rules: List<Rule> = listOf(
        ContactInfoRule(),
        SectionRule(),
        LengthRule(),
        MetricsRule(),
        BulletQualityRule(),
        KeywordMatchRule()
    )

    fun analyze(resumeText: String, jobText: String?, jobTopN: Int = 20): Report {
        val normalizedResume = TextUtils.normalize(resumeText)

        val jobKeywords = jobText?.let {
            val normalizedJob = TextUtils.normalize(it)
            TextUtils.extractKeywords(normalizedJob, topN = jobTopN)
        } ?: emptyList()

        val context = AnalysisContext(
            resumeRaw = resumeText,
            resume = normalizedResume,
            jobRaw = jobText,
            job = jobText?.let(TextUtils::normalize),
            jobKeywords = jobKeywords
        )

        val findings = rules.flatMap { it.evaluate(context) }

        // Score is computed from weighted findings.
        // Start at 100 then apply penalties; add small bonuses for strong indicators.
        var score = 100

        for (f in findings) {
            score -= f.penalty
            score += f.bonus
        }

        score = min(100, max(0, score))

        return Report(
            score = score,
            summary = buildSummary(score),
            findings = findings.sortedWith(
                compareByDescending<Finding> { it.severity.rank }
                    .thenByDescending { it.penalty }
                    .thenBy { it.title }
            ),
            jobKeywords = jobKeywords
        )
    }

    private fun buildSummary(score: Int): String {
        return when {
            score >= 90 -> "Excellent ATS readiness. Minor tweaks can make it even sharper."
            score >= 75 -> "Good overall. A few targeted improvements will boost clarity and ATS match."
            score >= 60 -> "Fair. Strengthen structure, measurable impact, and keyword alignment."
            else -> "Needs work for ATS and clarity. Focus on sections, bullet quality, and keywords."
        }
    }
}

data class AnalysisContext(
    val resumeRaw: String,
    val resume: String,
    val jobRaw: String?,
    val job: String?,
    val jobKeywords: List<String>
)
