# AI-Like Resume Analyzer (Kotlin + JavaFX)

A desktop application that analyzes resumes against job descriptions and provides ATS-style feedback using a modular, rule-based engine.  
The application runs completely offline and supports **TXT, PDF, and DOCX** resume uploads.

This project was built to demonstrate clean architecture, rule-based systems, file parsing, and JavaFX UI development using **Kotlin and JDK 21**.

---

## Features

- Upload resumes in **TXT, PDF, or DOCX** format
- Upload an optional **job description**
- Generates an **overall resume score (0–100)**
- Provides actionable feedback such as:
  - Missing or weak resume sections
  - Lack of measurable impact (metrics)
  - Weak bullet phrasing
  - Resume length issues
  - Keyword alignment with job descriptions
- Extracts and analyzes **top job keywords**
- Fully local processing (no external APIs or cloud services)

---

## Supported File Formats

- **TXT** – Plain text resumes  
- **DOCX** – Microsoft Word resumes  
- **PDF** – Text-based PDFs  

> Note: Scanned or image-only PDFs are not supported (OCR is intentionally not included).

---

## Tech Stack

- **Language:** Kotlin  
- **JDK:** 21  
- **UI:** JavaFX  
- **Build Tool:** Gradle (Kotlin DSL)  
- **Libraries:**
  - Apache POI (DOCX parsing)
  - Apache PDFBox (PDF parsing)

---

## Architecture Overview

The project follows a **clean, modular design** with clear separation of concerns.
com.example.resumeanalyzer
├── MainApp.kt # JavaFX UI entry point
├── Analyzer.kt # Orchestrates resume analysis
├── Report.kt # Formats analysis results
├── TextUtils.kt # Text processing utilities
├── DocumentTextExtractor.kt # TXT / PDF / DOCX extraction
└── rules
├── Rule.kt # Rule interface
├── SectionRule.kt # Checks required sections
├── MetricsRule.kt # Detects measurable impact
├── BulletQualityRule.kt # Analyzes bullet phrasing
├── KeywordMatchRule.kt # Job keyword alignment
├── ContactInfoRule.kt # Email / phone / LinkedIn checks
└── LengthRule.kt # Resume length validation

### Design Rationale

- The **core analyzer** is UI-agnostic
- Each resume check is implemented as an **independent rule**
- New rules can be added without modifying existing logic
- The UI layer only handles **file input and result display**

This design mirrors how rule-based evaluation engines are structured in real-world systems.

---

## How Scoring Works

- The analysis starts with a base score of **100**
- Each rule applies penalties or bonuses
- The final score is clamped between **0 and 100**

### Severity Levels
- **INFO** – Informational feedback
- **LOW** – Minor improvements
- **MEDIUM** – Noticeable issues
- **HIGH** – Critical ATS or quality problems

---

## Application Screenshots

> The screenshots below demonstrate the JavaFX UI and example analysis output.

### Resume Upload Interface
![Resume Upload Screen](screenshots/upload-screen.png)

### Analysis Output
![Analysis Result](screenshots/analysis-result.png)

### Example Low-Scoring Resume
![Low Score Example](screenshots/low-score-example.png)

---

## How to Run the Project

### Prerequisites
- **JDK 21** installed
- Internet connection (first run only, to download dependencies)

### Steps

```bash
./gradlew run

### Package Structure

