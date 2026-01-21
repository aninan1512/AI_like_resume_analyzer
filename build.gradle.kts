plugins {
    kotlin("jvm") version "2.0.21"
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

javafx {
    version = "21.0.4"
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("com.example.resumeanalyzer.MainAppKt")
}

dependencies {
    testImplementation(kotlin("test"))

    // PDF support
    implementation("org.apache.pdfbox:pdfbox:2.0.30")

    // DOCX support (Apache POI) + common runtime dependencies
    implementation("org.apache.poi:poi-ooxml:5.2.5")
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.apache.commons:commons-compress:1.26.2")
    implementation("org.apache.xmlbeans:xmlbeans:5.2.1")
}

tasks.test {
    useJUnitPlatform()
}
