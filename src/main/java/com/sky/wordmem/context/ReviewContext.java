package com.sky.wordmem.context;
import com.sky.wordmem.Exceptions;
import com.sky.wordmem.entity.WordEntity;
import com.sky.wordmem.module.NotebookModule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/4/20
 * @description com.sky.wordmem.context
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
class ReviewContext extends BaseContext {

    private static final int LIFETIME = 3; // hours
    private Date beginTime;

    @Autowired
    private NotebookModule notebook;
    //用来复习的上下文

    @Override
    public void init(){
        beginTime = new Date();
        try {
            int SECOND = 1000;

            Thread.sleep(SECOND);
            screen.render("要开始咯 ~ 请清空你的小脑瓜");
            Thread.sleep(SECOND);
            screen.render("3");
            Thread.sleep(SECOND);
            screen.render("2");
            Thread.sleep(SECOND);
            screen.render("1");
            Thread.sleep(SECOND);
            screen.render("GO!!!\n");

        } catch (InterruptedException e) {
            throw Exceptions.systemException(e.toString());
        }
        cursor = 0;
        List<WordEntity> words = notebook.review();
        List<String> values = words.stream().map(WordEntity::getValue)
                .collect(Collectors.toList());
        this.words = words;
        this.values = values;
        this.history = new LinkedList<>();
        this.scanner = new Scanner(System.in);
        this.inited = true;
    }

    @Override
    void close() {

    }
}
