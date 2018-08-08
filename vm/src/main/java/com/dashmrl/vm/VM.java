package com.dashmrl.vm;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author       xinliu
 * Date         1/3/18
 * Time         7:37 PM
 * Email        xinliugm@gmail.com
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.CONSTRUCTOR)
@Documented
public @interface VM {
    boolean withType() default false;
}
