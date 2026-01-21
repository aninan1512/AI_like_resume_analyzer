package com.example.resumeanalyzer.rules

import com.example.resumeanalyzer.AnalysisContext

class ContactInfoRule : Rule {
    private val emailRegex = Regex("""\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}\b""", RegexOption.IGNORE_CASE)
    private val phoneRegex = Regex("""(\+\d{1,3}\s?)?(\(?\d{3}\)?[\s.-]?)\d{3}[\s.-]?\d{4}""")
    private val linkedInRegex = Regex("""\blinkedin\.com/in/\S+""", RegexOption.IGNORE_CASE)

    override fun evaluate(ctx: AnalysisContext): List<Finding> {
        val findings = mutableListOf<Finding>()

        val hasEmail = emailRegex.containsMatchIn(ctx.resumeRaw)
        val hasPhone = phoneRegex.containsMatchIn(ctx.resumeRaw)
        val hasLinkedIn = linkedInRegex.containsMatchIn(ctx.resumeRaw)

        if (!hasEmail) {
            findings += Finding(
                title = "Email not detected",
                message = "No email address detected in the resume text.",
                suggestion = "Include a professional email near the top.",
                severity = Severity.HIGH,
                penalty = 10
            )
        }

        if (!hasPhone) {
            findings += Finding(
                title = "Phone number not detected",
                message = "No phone number detected in the resume text.",
                suggestion = "Include a reachable phone number near the top.",
                severity = Severity.MEDIUM,
                penalty = 5
            )
        }

        if (!hasLinkedIn) {
            findings += Finding(
                title = "LinkedIn URL not detected",
                message = "No LinkedIn profile URL detected.",
                suggestion = "Add your LinkedIn (and optionally GitHub) URL in the header.",
                severity = Severity.LOW,
                penalty = 2
            )
        } else {
            findings += Finding(
                title = "LinkedIn detected",
                message = "A LinkedIn profile URL appears to be present.",
                severity = Severity.INFO,
                bonus = 1
            )
        }

        return findings
    }
}
