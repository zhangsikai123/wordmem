package com.sky.wordmem.config;

import com.sky.wordmem.crontask.AbstractScheduledTask;
import com.sky.wordmem.crontask.ScheduledTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/7/20
 * @description com.sky.wordmem.scheduler
 */
@EnableScheduling
@Configuration
@Slf4j
@Profile({"default"})
public class Scheduler implements SchedulingConfigurer {

    @Autowired
    private ScheduledTaskManager scheduledTaskManager;

    private void logCronTask(CronTask cronTask) {
        if (cronTask.getRunnable() instanceof ScheduledMethodRunnable) {
            log.info("CronTask: {} - {}", ((ScheduledMethodRunnable) cronTask.getRunnable()).getMethod().getDeclaringClass().getSimpleName(),
                    cronTask.getExpression());
        } else {
            log.warn("CronTask: {} - {}", cronTask.getRunnable().getClass().getSimpleName(), cronTask.getExpression());
        }
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
    {
        TaskScheduler taskScheduler = taskScheduler();
        taskRegistrar.setTaskScheduler(taskScheduler);
        // register cron tasks
        for(Pair<AbstractScheduledTask, String> pair: scheduledTaskManager.getScheduledTaskList()){
            taskRegistrar.addCronTask(pair.getKey(), pair.getValue());
        }
        if (taskRegistrar.hasTasks()) {
            log.info("configure scheduled task...");
            List<CronTask> cronTasks = taskRegistrar.getCronTaskList();
            if (!cronTasks.isEmpty()) {
                for (CronTask cronTask : cronTasks) {
                    logCronTask(cronTask);
                }
            }
            //other task
        }
    }

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler()
    {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);
        scheduler.setThreadNamePrefix("task-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }
}
