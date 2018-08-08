package com.dashmrl.vmprocessor;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         12:35 PM
 * Email        xinliugm@gmail.com
 */

public class AutoVMValidator {
    private com.dashmrl.vmprocessor.Logger logger;
    private Types typeUtils;

    public AutoVMValidator(Logger logger, Types typeUtils) {
        this.logger = logger;
        this.typeUtils = typeUtils;
        logger.note("" + (typeUtils == null));
    }

    public boolean isValid(Element element) {
//
        if (element.getKind() != ElementKind.CONSTRUCTOR) {
            logger.error("VM should not be used on non constructor element", element);
            return false;
        }
        Element enclosingElement = element.getEnclosingElement();
        Set<Modifier> modifiers = enclosingElement.getModifiers();
        for (Modifier modifier : modifiers) {
            if (modifier == Modifier.ABSTRACT || modifier == Modifier.PRIVATE) {
                logger.error(enclosingElement.toString() + " cannot be modified with private or abstract", element);
                return false;
            }
        }

        TypeMirror typeMirror = enclosingElement.asType();
        boolean subvm = false;
        while (typeMirror != null) {
            if ((Constants.ViewModel.toString()).equals(typeMirror.toString())) {
                subvm = true;
                break;
            }
            TypeElement t = (TypeElement) typeUtils.asElement(typeMirror);
            if (t != null) {
                typeMirror = t.getSuperclass();
            } else {
                break;
            }
        }
        if (!subvm) {
            logger.error(enclosingElement.toString() + " is not the subClass of android.arch.lifecycle.ViewModel", enclosingElement);
            return subvm;
        }
        return true;
    }


}
