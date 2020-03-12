package com.sky.wordmem.handlers;

import com.sky.wordmem.context.ContextManager;
import com.sky.wordmem.entity.WordEntity;
import com.sky.wordmem.module.NotebookModule;
import com.sky.wordmem.module.ScreenModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/4/20
 * @description com.sky.wordmem.handlers
 */
@Configuration
public class HandlerFactory {

    @Autowired
    ScreenModule screenModule;
    @Autowired
    ContextManager cm;
    @Autowired
    private NotebookModule notebook;
    @Autowired
    private ScreenModule screen;

    @Bean
    @Command({"last", "l"})
    public Handler lastWord(){
        return (cxt, r) -> {
            cm.context().deCursor();
            cm.context().renderCurrentWord();
        };
    }

    @Bean
    @Command({"show chinese", "sc"})
    public Handler showChinese() {
        return (cxt, r) -> {
            screenModule.setNoChinese(false);
            cm.context().renderCurrentWord();
        };
    }

    @Bean
    @Command({"no chinese", "nc"})
    public Handler noChinese() {
        return (cxt, r) -> {
            screenModule.setNoChinese(true);
            cm.context().renderCurrentWord();
        };
    }

    @Bean
    @Command({"today"})
    public Handler today() {
        // make sure the context is under default
        return (cxt, r) -> {
            cm.reset();
            List<WordEntity> todayProgress = notebook.todayProgress();
            screen.renderTodayProgress(todayProgress);
            if (!CollectionUtils.isEmpty(todayProgress)) {
                cm.context().setForceCommand(todayProgress
                        .get(todayProgress.size() - 1).getValue());
            }
        };
    }

    @Bean
    @Command({"yesterday"})
    public Handler yesterday() {
        return (cxt, r) -> {
            cm.reset();
            List<WordEntity> todayProgress = notebook.nDaysBeforeProgress(1);
            screen.renderTodayProgress(todayProgress);
            if (!CollectionUtils.isEmpty(todayProgress)) {
                cm.context().setForceCommand(todayProgress
                        .get(todayProgress.size() - 1).getValue());
            }
        };
    }
}
