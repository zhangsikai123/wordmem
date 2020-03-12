package com.sky.wordmem.handlers;

import com.sky.wordmem.Request;
import com.sky.wordmem.context.BaseContext;
import com.sky.wordmem.context.ContextManager;
import com.sky.wordmem.module.NotebookModule;
import com.sky.wordmem.module.ScreenModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.sky.wordmem.context.ContextManager.DEFAULT;
import static com.sky.wordmem.context.ContextManager.REVIEW;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/4/20
 * @description com.sky.wordmem.handlers
 */
@Command({"review", "rv"})
@Component
public class ReviewHandler implements Handler {
    @Autowired
    NotebookModule notebook;
    @Autowired
    ScreenModule screen;
    @Autowired
    ContextManager cm;

    private boolean foreGround;

    /**
     * 从三天前开始复习
     **/
    @Override
    public void handle(BaseContext cxt, Request ignored) {
        if (!foreGround) {
            foreGround = true;
            cm.useContext(REVIEW);
        } else {
            foreGround = false;
            cm.reset();
        }
        cm.context().renderCurrentWord();
    }
}
