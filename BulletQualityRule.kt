package com.example.resumeanalyzer.rules

import com.example.resumeanalyzer.AnalysisContext
import com.example.resumeanalyzer.TextUtils

class BulletQualityRule : Rule {
    private val weakStarters = listOf("responsible for", "worked on", "helped", "assisted", "involved in")
    private val strongVerbs = listOf(
        "built","developed","designed","implemented","optimized","automated","delivered","led","owned","improved",
        "reduced","increased","analyzed","created","migrated","integrated","refactored","tested","deployed"
    )

    override fun evaluate(ctx: AnalysisContext): List<Finding> {
        val lines = TextUtils.lines(ctx.resumeRaw)
        val bullets = lines.map { it.trim() }
            .filter { it.startsWith("-") || it.startsWith("•") || it.startsWith("*") }
            .map { it.trimStart('-', '•', '*', ' ') }

        if (bullets.isEmpty()) return emptyList()

        val weak = bullets.count { b -> TextUtils.containsAnyIgnoreCase(b, weakStarters) }
        val strong = bullets.count { b -> TextUtils.containsAnyIgnoreCase(b, strongVerbs) }

        val findings = mutableListOf<Finding>()

        if (weak >= 2) {
            findings += Finding(
                title = "Weak bullet phrasing detected",
                message = "Several bullets start with vague phrasing (e.g., 'responsible for', 'helped').",
                suggestion = "Start bullets with strong action verbs + outcome (Built/Improved/Reduced/Automated…).",
                severity = Severity.MEDIUM,
                penalty = 6
            )
        }

        if (strong.toDouble() / bullets.size.toDouble() >= 0.5) {
            findings += Finding(
                title = "Strong action verbs used",
                message = "Many bullets use action verbs, which improves clarity and impact.",
                severity = Severity.INFO,
                bonus = 3
            )
        } else {
            findings += Finding(
                title = "Improve action verbs",
                message = "Many bullets could be stronger with clearer action verbs.",
                suggestion = "Rewrite bullets using action verbs + tools + measurable impact.",
                severity = Severity.LOW,
                penalty = 2
            )
        }

        // Length check for bullets (too long hurts scanability)
        val tooLong = bullets.count { TextUtils.countWords(it) > 28 }
        if (tooLong >= 2) {
            findings += Finding(
                title = "Some bullets may be too long",
                message = "Multiple bullets exceed ~28 words, which can reduce scanability.",
                suggestion = "Keep most bullets to 1–2 lines. Split long bullets into two focused bullets.",
                severity = Severity.LOW,
                penalty = 3
            )
        }

        return findings
    }
}
