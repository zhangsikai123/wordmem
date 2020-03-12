package com.sky.wordmem.module;

import com.sky.wordmem.Exceptions;
import com.sky.wordmem.context.ContextManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/9/20
 * @description com.sky.wordmem.module
 */
public class TimerModule {
    private int startCursor;
    private boolean on;
    @Autowired
    ScreenModule screen;
    @Autowired
    ContextManager cm;


}
