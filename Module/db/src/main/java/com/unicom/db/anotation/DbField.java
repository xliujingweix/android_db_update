package com.unicom.db.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liut46 on 2017/10/31.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbField {
    String column();
    String type() default "TEXT";
    boolean unique() default false;
    boolean notNull() default false;
}
