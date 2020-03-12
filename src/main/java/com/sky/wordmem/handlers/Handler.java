package com.sky.wordmem.handlers;

import com.sky.wordmem.Request;
import com.sky.wordmem.context.BaseContext;
/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/2/20
 * @description com.sky.wordmem.handlers
 */
public interface Handler {
    void handle(BaseContext cxt, Request request);
}
