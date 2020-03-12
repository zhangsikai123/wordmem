package com.sky.wordmem.crontask;

import com.sky.wordmem.annotation.ScheduledTask;
import com.sky.wordmem.config.Lucene;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/7/20
 * @description com.sky.wordmem.crontask
 */
@ScheduledTask(taskName = "refresh_index_reader", cron = "0 * * ? * *")
@Component
@Slf4j
public class RefreshIndexReader extends AbstractScheduledTask {

    @Autowired
    Lucene lucene;

    @Override
    protected void runTask() {
        lucene.refreshSearcher();
    }
}
