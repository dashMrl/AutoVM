package com.dashmrl.vmprocessor;

import com.dashmrl.vm.VM;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         12:33 PM
 * Email        xinliugm@gmail.com
 */

public class FactoryClass {
    static Logger logger;
    static Elements elementUtils;
    private VM autoVM;
    private PackageElement packageElement;
    private TypeElement typeElement;
    private List<VariableElement> paramClasses = new ArrayList<>();
    private static final String SUFFIX = "_VMFACTORY";


    public FactoryClass(PackageElement packageElement, TypeElement typeElement,
                        List<? extends VariableElement> parameters, VM autoVM) {
        this.autoVM = autoVM;
        this.packageElement = packageElement;
        this.paramClasses.clear();
        this.paramClasses.addAll(parameters);
        this.typeElement = typeElement;
    }


    public JavaFile generateCode() {
        TypeSpec.Builder builder = creteTypeBuilder();
        builder.addMethod(createConstructor())
                .addMethod(createCreate());
        if (autoVM.withType()) {
            builder.addMethod(createGetType());
        }
        TypeSpec typeSpec = builder.build();
        return JavaFile.builder(packageElement.toString(), typeSpec).build();
    }

    private MethodSpec createConstructor() {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        for (VariableElement variableElement : paramClasses) {
            String name = variableElement.toString();
            List<AnnotationSpec> annotationSpecs = new ArrayList<>();
            for (AnnotationMirror annotationMirror : variableElement.getAnnotationMirrors()) {
                annotationSpecs.add(AnnotationSpec.get(annotationMirror));
            }
            constructorBuilder.addParameter(TypeName.get(variableElement.asType()).annotated(annotationSpecs), name);
            constructorBuilder.addStatement("this.$L = $L", name, name);
        }
        return constructorBuilder.build();
    }

    private MethodSpec createCreate() {

        return MethodSpec.methodBuilder("create")
                .addAnnotation(ClassName.get(Constants.NonNull.packageName, Constants.NonNull.className))
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(
                        TypeVariableName.get("T", ClassName.get(Constants.ViewModel.packageName, Constants.ViewModel.className))
                )
                .returns(TypeVariableName.get("T"))
                .addParameter(
                        ParameterizedTypeName
                                .get(
                                        ClassName.get(Class.class),
                                        TypeVariableName.get("T"))
                                .annotated(AnnotationSpec.builder(ClassName.get(Constants.NonNull.packageName, Constants.NonNull.className)).build()),
                        "modelClass")
                .beginControlFlow("if(modelClass.isAssignableFrom($L.class))", TypeName.get(typeElement.asType()).toString())
                .beginControlFlow("try")
                .addCode(generateCreateCode())
                .nextControlFlow("catch(Exception e)")
                .addStatement("e.printStackTrace()")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return super.create(modelClass)")
                .build();
    }


    private CodeBlock generateCreateCode() {
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.add("return modelClass.getConstructor(");
        int size = paramClasses.size();
        for (int i = 0; i < size; i++) {
            VariableElement param = paramClasses.get(i);
            builder.add(TypeName.get(param.asType()).toString()).add(".class");
            if (i < size - 1) {
                builder.add(",");
            }
        }
        builder.add(").newInstance(");
        for (int i = 0; i < size; i++) {
            builder.add(paramClasses.get(i).toString());
            if (i < size - 1) {
                builder.add(",");
            }
        }
        builder.add(");");
        builder.indent();
        return builder.build();
    }

    private MethodSpec createGetType() {

        return MethodSpec.methodBuilder("getType")
                .addModifiers(Modifier.PUBLIC)
                .returns(
                        ParameterizedTypeName.get(
                                ClassName.get(Class.class),
                                TypeVariableName.get(typeElement.asType()))
                )
                .addStatement("return $L.class", TypeName.get(typeElement.asType()).toString())
                .build();
    }

    private TypeSpec.Builder creteTypeBuilder() {
        TypeSpec.Builder classBuilder =
                TypeSpec.classBuilder(getTypeName(typeElement))
                        .addModifiers(Modifier.PUBLIC)
                        .superclass(TypeName.get(elementUtils.getTypeElement(Constants.ViewModelProvider_Factory.toString()).asType()));
        for (VariableElement paramClass : paramClasses) {
            classBuilder.addField(TypeName.get(paramClass.asType()), paramClass.toString(), Modifier.PRIVATE);
        }
        return classBuilder;
    }


    private String getTypeName(Element element) {
        Element enclosingElement = element.getEnclosingElement();
        while (enclosingElement != null && enclosingElement.getKind() != ElementKind.PACKAGE) {
            enclosingElement = enclosingElement.getEnclosingElement();
        }
        String origin = element.toString();
        return origin.replace(enclosingElement.toString(), "").substring(1).replace(".", "_").concat(SUFFIX);
    }
}
