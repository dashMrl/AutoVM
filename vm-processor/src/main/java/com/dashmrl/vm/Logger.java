package com.dashmrl.vm;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

/**
 * Author       xinliu
 * Date         1/3/18
 * Time         8:14 PM
 * Email        xinliugm@gmail.com
 */

public final class Logger {
    private static Logger INSTANCE;
    private Messager messager;

    private Logger(Messager messager) {
        this.messager = messager;
    }


    public static synchronized Logger get(Messager messager) {
        INSTANCE = new Logger(messager);
        return INSTANCE;
    }


    public void other(String msg) {
        printMessage(Kind.OTHER, msg);
    }

    public void other(String msg, Element element) {
        printMessage(Kind.OTHER, msg, element);
    }

    public void other(String msg, Element element, AnnotationMirror mirror) {
        printMessage(Kind.OTHER, msg, element, mirror);

    }

    public void other(String msg, Element element, AnnotationMirror mirror, AnnotationValue value) {
        printMessage(Kind.OTHER, msg, element, mirror, value);
    }


    public void note(String msg) {
        printMessage(Kind.NOTE, msg);
    }

    public void note(String msg, Element element) {
        printMessage(Kind.NOTE, msg, element);
    }

    public void note(String msg, Element element, AnnotationMirror mirror) {
        printMessage(Kind.NOTE, msg, element, mirror);

    }

    public void note(String msg, Element element, AnnotationMirror mirror, AnnotationValue value) {
        printMessage(Kind.NOTE, msg, element, mirror, value);
    }


    public void mandatoryWran(String msg) {
        printMessage(Kind.MANDATORY_WARNING, msg);
    }

    public void mandatoryWran(String msg, Element element) {
        printMessage(Kind.MANDATORY_WARNING, msg, element);
    }

    public void mandatoryWran(String msg, Element element, AnnotationMirror mirror) {
        printMessage(Kind.MANDATORY_WARNING, msg, element, mirror);

    }

    public void mandatoryWran(String msg, Element element, AnnotationMirror mirror, AnnotationValue value) {
        printMessage(Kind.MANDATORY_WARNING, msg, element, mirror, value);
    }


    public void warn(String msg) {
        printMessage(Kind.WARNING, msg);

    }

    public void warn(String msg, Element element) {
        printMessage(Kind.WARNING, msg, element);
    }

    public void warn(String msg, Element element, AnnotationMirror mirror) {
        printMessage(Kind.WARNING, msg, element, mirror);

    }

    public void warn(String msg, Element element, AnnotationMirror mirror, AnnotationValue value) {
        printMessage(Kind.WARNING, msg, element, mirror, value);
    }

    public void error(String msg) {
        printMessage(Kind.ERROR, msg);
    }

    public void error(String msg, Element element) {
        printMessage(Kind.ERROR, msg, element);
    }

    public void error(String msg, Element element, AnnotationMirror mirror) {
        printMessage(Kind.ERROR, msg, element, mirror);
    }

    public void error(String msg, Element element, AnnotationMirror mirror, AnnotationValue value) {
        printMessage(Kind.ERROR, msg, element, mirror, value);
    }


    private void printMessage(Kind kind, CharSequence msg) {
        messager.printMessage(kind, msg);
    }

    private void printMessage(Kind kind, CharSequence msg, Element element) {
        messager.printMessage(kind, msg, element);

    }

    private void printMessage(Kind kind, CharSequence msg, Element element, AnnotationMirror mirror) {
        messager.printMessage(kind, msg, element, mirror);

    }

    private void printMessage(Kind kind, CharSequence msg, Element element, AnnotationMirror mirror, AnnotationValue value) {
        messager.printMessage(kind, msg, element, mirror, value);

    }
}


