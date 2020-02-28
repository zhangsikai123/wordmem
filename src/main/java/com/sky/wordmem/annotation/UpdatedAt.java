package com.sky.wordmem.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/24/20
 * @description com.sky.wordmem.annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UpdatedAt {
    String value() default "";
}
