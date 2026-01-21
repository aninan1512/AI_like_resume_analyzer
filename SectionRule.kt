package com.example.resumeanalyzer.rules

import com.example.resumeanalyzer.AnalysisContext
import com.example.resumeanalyzer.TextUtils

class SectionRule : Rule {
    private val recommendedSections = listOf(
        "experience", "work experience", "projects", "education", "skills"
    )

    private val niceToHave = listOf(
        "summary", "professional summary", "certifications", "certification",
        "achievements", "leadership", "volunteer"
    )

    override fun evaluate(ctx: AnalysisContext): List<Finding> {
        val resumeLower = TextUtils.lower(ctx.resume)
        val findings = mutableListOf<Finding>()

        val missingCore = recommendedSections.filterNot { it in resumeLower }
        if (missingCore.isNotEmpty()) {
            findings += Finding(
                title = "Missing core sections",
                message = "Your resume may be missing: ${missingCore.joinToString(", ")}.",
                suggestion = "Add clear section headers (e.g., EXPERIENCE, PROJECTS, EDUCATION, SKILLS).",
                severity = Severity.HIGH,
                penalty = 12
            )
        } else {
            findings += Finding(
                title = "Core sections present",
                message = "Core sections (Experience/Projects/Education/Skills) appear to be present.",
                severity = Severity.INFO,
                bonus = 2
            )
        }

        val missingNice = niceToHave.filterNot { it in resumeLower }
        if (missingNice.size == niceToHave.size) {
            findings += Finding(
                title = "No summary or supporting sections detected",
                message = "No Summary/Achievements/Certifications-style sections detected.",
                suggestion = "Consider adding a 2–3 line summary and/or a Certifications section (if relevant).",
                severity = Severity.LOW,
                penalty = 3
            )
        }

        return findings
    }
}
