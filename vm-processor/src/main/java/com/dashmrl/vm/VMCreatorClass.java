package com.dashmrl.vm;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * @author xavier <a href="mailto:xinliugm@gmail.com">Contact me.</a>
 * @since 2018/8/19 20:54
 */
public class VMCreatorClass {
    private static final String SUFFIX = "$Creator";
    private Elements elementUtils;
    private String canonicalName;
    private List<ExecutableElement> constructors;

    public VMCreatorClass(Elements elementUtils, String canonicalName, List<ExecutableElement> constructors) {
        this.elementUtils = elementUtils;
        this.canonicalName = canonicalName;
        this.constructors = constructors;
    }


    public JavaFile generateCode() {
        return JavaFile
                .builder(
                        vmPackageName(),
                        generateCreatorClass())
                .build();
    }


    private TypeSpec generateCreatorClass() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(vmClassName() + SUFFIX)
                .addJavadoc("Automatically generated file. DO NOT MODIFY")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.FINAL)
                .superclass(
                        ParameterizedTypeName.get(
                                ClassName.get(Constants.BaseCreator.packageName, Constants.BaseCreator.className),
                                TypeName.get(elementUtils.getTypeElement(canonicalName).asType())
                        )
                )
                .addMethod(generateConstructor())
                .addMethod(generateCreate());
        return builder.build();
    }

    private MethodSpec generateConstructor() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("constructorParamTypess = new Class<?>[$L][]", constructors.size());
        for (int i = 0; i < constructors.size(); i++) {
            List<? extends VariableElement> parameters = constructors.get(i).getParameters();
            StringBuilder elementsBuilder = new StringBuilder();
            for (int j = 0; j < parameters.size(); j++) {
                if (j != 0) {
                    elementsBuilder.append(',');
                }
                elementsBuilder.append(TypeName.get(parameters.get(j).asType()).box().toString())
                        .append(".class");
            }
            builder.addStatement("constructorParamTypess[$L] = new Class<?>[]{$L}", i, elementsBuilder.toString());
        }
        return builder.build();
    }

    private MethodSpec generateCreate() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("create")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(elementUtils.getTypeElement(canonicalName).asType()))
                .addParameter(Object[].class, "params")
                .addCode(generateSwitch());
        return builder.build();
    }

    private CodeBlock generateSwitch() {
        CodeBlock.Builder builder = CodeBlock.builder()
                .addStatement("int index = findMethodIndex(params)")
                .beginControlFlow("switch(index)"); // switch begin

        for (int i = 0; i < constructors.size(); i++) {
            ExecutableElement executableElement = constructors.get(i);
            List<? extends VariableElement> parameters = executableElement.getParameters();
            boolean isPrivate = executableElement.getModifiers().contains(Modifier.PRIVATE);
            builder.beginControlFlow("case $L:", i); // case begin
            if (isPrivate) {
                builder.beginControlFlow("try");
                StringBuilder getConstructorBuilder = new StringBuilder("$T<$T> constructor  = $T.class.getDeclaredConstructor(");
                for (int j = 0; j < parameters.size(); j++) {
                    if (j != 0) {
                        getConstructorBuilder.append(",");
                    }
                    getConstructorBuilder.append(getParamCanonicalName(TypeName.get(parameters.get(j).asType()).toString())).append(".class");
                }
                getConstructorBuilder.append(")");
                builder.addStatement(getConstructorBuilder.toString(), Constructor.class, ClassName.get(vmPackageName(), vmClassName()), ClassName.get(vmPackageName(), vmClassName()))
                        .addStatement("constructor.setAccessible(true)");
                StringBuilder newInstanceBuilder = new StringBuilder("$T vm = constructor.newInstance(");
                for (int j = 0; j < parameters.size(); j++) {
                    if (j != 0) {
                        newInstanceBuilder.append(",");
                    }
                    newInstanceBuilder.append("params[").append(j).append("]");
                }
                newInstanceBuilder.append(")");
                builder.addStatement(newInstanceBuilder.toString(), ClassName.get(vmPackageName(), vmClassName()))
                        .addStatement("constructor.setAccessible(false)")
                        .addStatement("return vm")
                        .nextControlFlow("catch($T | $T | $T | $T e)",
                                IllegalAccessException.class,
                                InstantiationException.class,
                                InvocationTargetException.class,
                                NoSuchMethodException.class)
                        .addStatement("e.printStackTrace()")
                        .addStatement("return null")
                        .endControlFlow();
            } else {
                StringBuilder newInstanceBuilder = new StringBuilder("return new $T(");
                for (int j = 0; j < parameters.size(); j++) {
                    if (j != 0) {
                        newInstanceBuilder.append(",");
                    }

                    newInstanceBuilder.append("((").append(getParamCanonicalName(TypeName.get(parameters.get(j).asType()).toString())).append(")params[").append(j).append("])");
                }
                newInstanceBuilder.append(")");
                builder.addStatement(newInstanceBuilder.toString(),
                        ClassName.get(vmPackageName(), vmClassName()));
            }
            builder.endControlFlow(); // case end
        }
        builder.endControlFlow() // switch end
                .addStatement("return null");
        return builder.build();
    }


    private String getParamCanonicalName(String origin) {
        int indexOf = origin.indexOf('<');
        if (indexOf == -1) {
            indexOf = origin.length();
        }
        origin = origin.substring(0, indexOf);

        indexOf = origin.indexOf("[L");
        if (indexOf == -1) {
            return origin;
        } else {
           return origin.substring(indexOf + 2) + Arrays.stream(new int[indexOf + 1]).mapToObj(i -> "[]").reduce(((s, s2) -> s + s2)).orElse("");
        }
    }


    private String vmPackageName() {
        return canonicalName.substring(0, canonicalName.lastIndexOf('.'));
    }

    private String vmClassName() {
        return canonicalName.substring(canonicalName.lastIndexOf('.') + 1);
    }

}
