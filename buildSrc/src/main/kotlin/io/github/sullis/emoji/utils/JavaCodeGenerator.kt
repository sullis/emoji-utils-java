package io.github.sullis.emoji.utils

import java.nio.file.Path
import java.io.IOException
import com.squareup.javapoet.*
import javax.lang.model.element.Modifier

class JavaCodeGenerator {
    private val packageName = "io.github.sullis.emoji.utils"

    @Throws(IOException::class)
    fun generate(outputPath: Path) {
        val files = buildJavaFiles()

        for (file in files) {
            file.writeTo(System.out)
            file.writeTo(outputPath)
        }
    }

    private fun buildJavaFiles(): List<JavaFile> {
        return listOf((buildEmojiStringBuilder()))
    }

    private fun buildEmojiStringBuilder(): JavaFile {
        val toStringMethod = MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.PUBLIC)
                .returns(String::class.java)
                .addStatement("return sb.toString()")
                .build()

        val appendMethod = MethodSpec.methodBuilder("append")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CharSequence::class.java, "cs")
                .returns(ClassName.bestGuess("$packageName.EmojiStringBuilder"))
                .addStatement("sb.append(cs)")
                .addStatement("return this")
                .build()

        val emojiStringBuilderType = TypeSpec.classBuilder("EmojiStringBuilder")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(appendMethod)
                .addMethod(toStringMethod)
                .addField(StringBuilder::class.java, "sb", Modifier.PRIVATE)
                .build()

        return JavaFile.builder(packageName, emojiStringBuilderType).build()
    }
}
