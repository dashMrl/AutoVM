package com.dashmrl.vm;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author xavier <a href="mailto:xinliugm@gmail.com">Contact me.</a>
 * @since 2018/8/19 22:41
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class VMProcessor extends AbstractProcessor {
    private Logger logger;
    private Filer filer;
    private Types typeUtils;
    private Elements elementUtils;
    private Map<String, String> options;
    private VMValidator validator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        options = processingEnvironment.getOptions();
        logger = Logger.get(processingEnvironment.getMessager());
        validator = new VMValidator(logger, typeUtils);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        VMCollector collector = new VMCollector();

        Set<? extends Element> elementsAnnotatedWithVM = roundEnvironment.getElementsAnnotatedWith(VM.class);
        for (Element element : elementsAnnotatedWithVM) {
            if (!validator.validate(element)) {
                return false;
            }
            Element vmClazzElement = element.getEnclosingElement();
            collector.addConstructor(
                    elementUtils.getPackageOf(vmClazzElement) + "." + vmClazzElement.getSimpleName(),
                    ((ExecutableElement) element));
        }

        for (String vmClass : collector.vmClasses()) {
            List<ExecutableElement> vmConstructors = collector.getVmConstructors(vmClass);
            VMCreatorClass vmCreatorClass = new VMCreatorClass(elementUtils, vmClass, vmConstructors);
            try {
                vmCreatorClass.generateCode().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(VM.class.getCanonicalName());
        return types;
    }
}
