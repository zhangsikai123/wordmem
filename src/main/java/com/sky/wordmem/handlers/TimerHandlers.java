package com.sky.wordmem.handlers;

import com.sky.wordmem.Exceptions;
import com.sky.wordmem.context.ContextManager;
import com.sky.wordmem.module.ScreenModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/9/20
 * @description com.sky.wordmem.handlers
 */
@Configuration
public class TimerHandlers {


    @Autowired
    private ScreenModule screen;

    @Bean
    @Command({"opentimer", "ot"})
    public Handler openTimer(){
        return (cxt, r) -> {
            cxt.openOrCloseTimer();
        };
    }

    @Bean
    @Command({"speed", "sp"})
    public Handler countSpeed(){
        return (cxt, r)-> {
            try {
                int wordsPerMinute = cxt.countSpeed();
                int words = cxt.wordsSinceTimerOn();
                long duration = cxt.minutesSinceTimerOn();
                String msg = String.format("时光已经过去 %s 分钟咯，您一共浏览了 %s个单词，平均 %s 个单词/分钟，加油!!",
                        duration, words, wordsPerMinute);
                screen.render(msg);
            }catch (Exceptions.TimerException ignored){
                screen.render("计时器妹打开呢傻瓜，输入 opentimer 或者 ot 打开它先");
            }
        };
    }
}
