package com.sky.wordmem.handlers;

import com.sky.wordmem.Request;
import com.sky.wordmem.context.BaseContext;
import org.springframework.stereotype.Component;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/2/20
 * @description com.sky.wordmem.handlers
 */
@Command
@Component
public class DefaultHandler implements Handler {
    @Override
    public void handle(BaseContext cxt, Request ignored) {
        cxt.addCursor();
        cxt.renderCurrentWord();
    }
}
