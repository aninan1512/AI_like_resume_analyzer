package com.example.resumeanalyzer.rules

import com.example.resumeanalyzer.AnalysisContext

interface Rule {
    fun evaluate(ctx: AnalysisContext): List<Finding>
}

enum class Severity(val rank: Int) {
    INFO(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3)
}

data class Finding(
    val title: String,
    val message: String,
    val suggestion: String = "",
    val severity: Severity = Severity.MEDIUM,
    val penalty: Int = 0,
    val bonus: Int = 0
)
