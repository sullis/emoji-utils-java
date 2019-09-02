package io.github.sullis.emoji.utils

import java.nio.file.Path
import java.io.IOException
import com.squareup.javapoet.*
import com.vdurmont.emoji.Emoji
import com.vdurmont.emoji.EmojiManager
import java.util.stream.Stream
import javax.lang.model.element.Modifier
import kotlin.streams.toList
import javax.lang.model.SourceVersion
import org.apache.commons.lang3.StringUtils

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

        val sbField = FieldSpec.builder(StringBuilder::class.java, "sb")
        sbField.addModifiers(Modifier.PRIVATE, Modifier.FINAL)
        sbField.initializer(CodeBlock.builder().addStatement("new StringBuilder()").build())

        val emojiStringBuilderType = TypeSpec.classBuilder("EmojiStringBuilder")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(appendMethod)
                .addMethods(buildEmojiMethods().toList())
                .addMethod(toStringMethod)
                .addField(sbField.build())

        return JavaFile.builder(packageName, emojiStringBuilderType.build())
                .build()
    }

    private fun buildEmojiMethods(): Stream<MethodSpec> {
        return EmojiManager.getAll().stream().map { emoji -> buildEmojiMethod(emoji) }
    }

    private fun buildEmojiMethod(emoji: Emoji): MethodSpec {
        val methodName = toJavaMethodName(emoji)
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.bestGuess("$packageName.EmojiStringBuilder"))
                .addStatement("sb.append(\$S)", emoji.unicode)
                .addStatement("return this")
                .build()
    }

    private fun isFlag(e: Emoji): Boolean {
      return (e.tags.size == 2) && e.tags.contains("flag")
    }

    private fun flagMethodName(e: Emoji): String {
        val words = e.tags.filterNot { it == "flag" }
        val sb = StringBuilder()
        sb.append("flag_")
        words.forEach {
            sb.append(it)
            sb.append("_")
        }
        return sb.toString()
    }

    private fun toJavaMethodName(e: Emoji): String {
        val candidateName = if (isFlag(e)) {
            flagMethodName(e)
        } else {
            e.description.trim()
        }
        return toJavaMethodName(candidateName)
    }

    private fun toJavaMethodName(input: String): String {
        val sb = StringBuilder()
        for (c in StringUtils.removeEnd(input, "_").toCharArray()) {
            val lowerCase = c.toLowerCase()
            if (Character.isJavaIdentifierPart(lowerCase)) {
                sb.append(lowerCase)
            } else {
                if (sb.isEmpty()) {
                    sb.append("_")
                } else {
                    if (sb.get(sb.length - 1) != '_') {
                        sb.append("_")
                    }
                }
            }
        }
        if (!SourceVersion.isName(sb)) {
            sb.append("_")
        }
        return sb.toString()
    }
}
