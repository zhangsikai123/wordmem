package com.sky.wordmem.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/7/20
 * @description com.sky.wordmem.annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ScheduledTask {
    String taskName();
    String cron();
}
