package com.sky.wordmem.crontask;

import com.sky.wordmem.annotation.ScheduledTask;
import com.sky.wordmem.context.ContextManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.sky.wordmem.context.ContextManager.REVIEW;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/8/20
 * @description com.sky.wordmem.crontask
 */
@ScheduledTask(taskName = "refresh_review_session", cron = "0 0 0/2 ? * *")
@Component
@Slf4j
public class RefreshReviewSession extends AbstractScheduledTask{
    @Autowired
    ContextManager contextManager;
    @Override
    protected void runTask() {
        if(REVIEW.equals(contextManager.getCurrentContextTag())){
            contextManager.refresh();
        }
    }
}
