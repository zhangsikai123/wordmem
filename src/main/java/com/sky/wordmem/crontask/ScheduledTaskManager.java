package com.sky.wordmem.crontask;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/7/20
 * @description com.sky.wordmem.crontask
 */
@Component
public class ScheduledTaskManager {

    private List<Pair<AbstractScheduledTask, String>> scheduledTaskList = new ArrayList<>();

    void registerTask(AbstractScheduledTask task, String cronExpression) {
        scheduledTaskList.add(Pair.of(task, cronExpression));
    }

    public List<Pair<AbstractScheduledTask, String>> getScheduledTaskList(){
        return this.scheduledTaskList;
    }
}
