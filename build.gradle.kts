
import io.github.sullis.emoji.utils.JavaCodeGenerator

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.5.0"

    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.vdurmont:emoji-java:5.1.1")

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

//    testImplementation("org.jetbrains.kotlin:kotlin-test")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
}

val generatedSrcDir = File(buildDir, "generated/sources/emoji-utils/")

sourceSets["main"].java {
    srcDir(generatedSrcDir.toString())
}

val javaCodegen = tasks.register("javaCodegen") {
    generatedSrcDir.mkdirs()
    doLast {
      try {
          JavaCodeGenerator().generate(generatedSrcDir.toPath())
      } catch (ex: Exception) {
          ex.printStackTrace()
          throw ex
      }
    }
}

tasks.withType<org.gradle.api.tasks.compile.JavaCompile>().configureEach {
  dependsOn(javaCodegen)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  dependsOn(javaCodegen)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Test> {
    useJUnitPlatform()
}
