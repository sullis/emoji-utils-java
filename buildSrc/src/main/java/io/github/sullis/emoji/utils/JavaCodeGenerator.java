package io.github.sullis.emoji.utils;

import java.nio.file.Path;
import java.io.File;
import java.io.IOException;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.ArrayList;

public class JavaCodeGenerator {
    private final String packageName = "io.github.sullis.emoji.utils";

    public void generate(Path outputPath) throws IOException {
        List<JavaFile> files = buildJavaFiles();

        for (JavaFile file : files) {
            file.writeTo(System.out);
            file.writeTo(outputPath);
        }
    }

    protected List<JavaFile> buildJavaFiles() {
        List<JavaFile> javaFiles = new ArrayList<JavaFile>();

        MethodSpec toStringMethod = MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return sb.toString()")
                .build();

        TypeSpec emojiStringBuilderType = TypeSpec.classBuilder("EmojiStringBuilder")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(toStringMethod)
                .addField(StringBuilder.class, "sb", Modifier.PRIVATE)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, emojiStringBuilderType)
                .build();

        javaFiles.add(javaFile);
        return javaFiles;
    }
}
