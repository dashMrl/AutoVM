package com.dashmrl.autovm;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class AutoVMProcessor extends AbstractProcessor {
    static Logger logger;
    private AutoVMValidator validator;
    private Filer filer;
    static Elements elementUtils;
    private Map<String, String> options;
    static Types typeUtils;
    private HashMap<String, FactoryClass> factoryClassHashMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        options = processingEnvironment.getOptions();
        logger = Logger.get(processingEnvironment.getMessager());
        validator = new AutoVMValidator(logger, typeUtils);
        FactoryClass.logger = logger;
        FactoryClass.elementUtils = elementUtils;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        factoryClassHashMap.clear();

        Set<? extends Element> autovmClasses = roundEnvironment.getElementsAnnotatedWith(AutoVM.class);
        for (Element autovmClass : autovmClasses) {
            if (!validator.isValid(autovmClass)) {
                return false;
            }
            TypeElement enclosingElement = (TypeElement) autovmClass.getEnclosingElement();
            FactoryClass factoryClass = factoryClassHashMap.get(enclosingElement.toString());
            if (factoryClass != null) {
                logger.error("only one constructor can be annotated with AutoVM", enclosingElement);
                return false;
            } else {
                factoryClass = new FactoryClass(
                        elementUtils.getPackageOf(enclosingElement),
                        enclosingElement,
                        ((ExecutableElement) autovmClass).getParameters(),
                        autovmClass.getAnnotation(AutoVM.class));
                factoryClassHashMap.put(enclosingElement.toString(), factoryClass);
            }
        }
        factoryClassHashMap.forEach((className, factory) -> {
            try {
                factory.generateCode().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(AutoVM.class.getCanonicalName());
        return types;
    }


    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
