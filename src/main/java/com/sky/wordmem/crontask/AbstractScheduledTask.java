package com.sky.wordmem.crontask;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/7/20
 * @description com.sky.wordmem.scheduler
 */

import javax.annotation.PostConstruct;
import com.sky.wordmem.Exceptions;
import com.sky.wordmem.annotation.ScheduledTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public abstract class AbstractScheduledTask implements Runnable {

    private String taskName;
    @Autowired
    private ScheduledTaskManager manager;

    protected abstract void runTask();

    public String getTaskName() {
        return this.taskName;
    }

    @Override
    public void run() {
        try {
            runTask();
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @PostConstruct
    public void registerSelf() {
        ScheduledTask taskAnnotation = getTaskAnnotation();
        this.taskName = taskAnnotation.taskName();
        String cron = taskAnnotation.cron();
        manager.registerTask(this, cron);
    }


    private ScheduledTask getTaskAnnotation() {
        ScheduledTask taskAnnotation = this.getClass().getAnnotation(ScheduledTask.class);
        if (taskAnnotation == null) {
            throw Exceptions.onStartException("loading scheduled task error: no annotation: ScheduledTask");
        }
        return taskAnnotation;
    }
}
