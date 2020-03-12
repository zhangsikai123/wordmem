package com.sky.wordmem.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/2/20
 * @description com.sky.wordmem.handlers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Command {
    String[] value() default {""};
}
