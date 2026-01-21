package com.example.resumeanalyzer

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

class MainApp : Application() {

    private var resumeFile: File? = null
    private var jobFile: File? = null

    override fun start(stage: Stage) {
        val resumeLabel = Label("No resume selected")
        val jobLabel = Label("No job description selected")

        val outputArea = TextArea().apply {
            isEditable = false
            prefHeight = 320.0
            wrapTextProperty().set(true)
        }

        val resumeButton = Button("Upload Resume (.txt/.pdf/.docx)").apply {
            setOnAction {
                resumeFile = chooseDocFile(stage, "Select Resume")
                resumeLabel.text = resumeFile?.name ?: "No resume selected"
            }
        }

        val jobButton = Button("Upload Job Description (.txt/.pdf/.docx) (Optional)").apply {
            setOnAction {
                jobFile = chooseDocFile(stage, "Select Job Description")
                jobLabel.text = jobFile?.name ?: "No job description selected"
            }
        }

        val analyzeButton = Button("Analyze").apply {
            setOnAction {
                val resume = resumeFile
                if (resume == null) {
                    showWarning("Please upload a resume first.")
                    return@setOnAction
                }

                try {
                    val resumeText = DocumentTextExtractor.extractText(resume)
                    val jobText = jobFile?.let { DocumentTextExtractor.extractText(it) }

                    if (resumeText.isBlank()) {
                        showWarning("Could not extract text from the resume file. Try a text-based PDF or a DOCX.")
                        return@setOnAction
                    }

                    val report = Analyzer().analyze(resumeText = resumeText, jobText = jobText)
                    outputArea.text = report.toConsoleString()
                } catch (e: Exception) {
                    showWarning("Failed to read file: ${e.message}")
                }
            }
        }

        val root = VBox(
            10.0,
            resumeButton,
            resumeLabel,
            jobButton,
            jobLabel,
            analyzeButton,
            outputArea
        ).apply {
            padding = Insets(15.0)
        }

        stage.scene = Scene(root, 720.0, 560.0)
        stage.title = "AI-like Resume Analyzer"
        stage.show()
    }

    private fun chooseDocFile(stage: Stage, title: String): File? {
        val chooser = FileChooser().apply {
            this.title = title
            extensionFilters.add(
                FileChooser.ExtensionFilter(
                    "Documents (*.txt, *.pdf, *.docx)",
                    "*.txt", "*.pdf", "*.docx"
                )
            )
        }
        return chooser.showOpenDialog(stage)
    }

    private fun showWarning(message: String) {
        Alert(Alert.AlertType.WARNING, message).showAndWait()
    }
}

fun main() {
    Application.launch(MainApp::class.java)
}

