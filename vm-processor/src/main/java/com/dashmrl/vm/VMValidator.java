package com.dashmrl.vm;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;

/**
 * @author xavier <a href="mailto:xinliugm@gmail.com">Contact me.</a>
 * @since 2018/8/19 19:04
 */
public class VMValidator {
    private com.dashmrl.vm.Logger logger;
    private Types typeUtils;

    public VMValidator(com.dashmrl.vm.Logger logger, Types typeUtils) {
        this.logger = logger;
        this.typeUtils = typeUtils;
    }

    public boolean validate(Element element) {
        boolean validate = validateElementType(element);
        if (!validate) {
            return false;
        }
        validate = validateEnclosingElement((ExecutableElement) element);
        if (!validate) {
            return false;
        }
        return true;
    }

    private boolean validateElementType(Element element) {
        boolean validate = element.getKind() == ElementKind.CONSTRUCTOR;
        if ( !validate) {
            logger.error("VM should not be used on non-constructor element", element);
        }
        return validate;
    }

    private boolean validateEnclosingElement(ExecutableElement element) {
        // get class which defines this constructor
        Element enclosingElement = element.getEnclosingElement();

        return validateEnclosingClass(element, (TypeElement) enclosingElement, 0);
    }

    private boolean validateEnclosingClass(Element target, TypeElement element, int depth) {
        Set<Modifier> modifiers = element.getModifiers();

        if (modifiers.contains(Modifier.PRIVATE)) {
            logger.error(target.getSimpleName().toString() + " should not defined as/within private element", target);
            return false;
        }
        if (depth == 0 && modifiers.contains(Modifier.ABSTRACT)) {
            logger.error(target.getSimpleName().toString() + " should not be modified with abstract", target);
            return false;
        }

        Element enclosingElement = element.getEnclosingElement();
        if (enclosingElement.getKind() == ElementKind.METHOD) {
            logger.error(target.getSimpleName().toString() + " should not be defined within non-class element", target);
            return false;
        }

        if (enclosingElement.getKind() == ElementKind.CLASS) {
            if (!modifiers.contains(Modifier.STATIC) || !modifiers.contains(Modifier.PUBLIC)) {
                logger.error(target.getSimpleName().toString()+" defined within class but not modified with static/public",target);
                return false;
            }
            return validateEnclosingClass(target, (TypeElement) enclosingElement, depth + 1);
        }
        return true;

    }
}
