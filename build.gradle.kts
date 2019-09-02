
import io.github.sullis.emoji.utils.JavaCodeGenerator

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.41"

    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

//    testImplementation("org.jetbrains.kotlin:kotlin-test")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")
}

val generatedSrcDir = File("generated/src/main/java/")

sourceSets["main"].java {
    srcDir(generatedSrcDir.toString())
}

task("generateEmojiStringBuilder") {
    generatedSrcDir.mkdirs()
    System.out.println("mkdirs() done.")
    try {
        JavaCodeGenerator().generate(generatedSrcDir.toPath())
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
