package com.sky.wordmem.module;

import com.sky.wordmem.dao.MarkDao;
import com.sky.wordmem.dao.NoteDao;
import com.sky.wordmem.dao.WordDao;
import com.sky.wordmem.entity.MarkEntity;
import com.sky.wordmem.entity.NoteEntity;
import com.sky.wordmem.entity.WordEntity;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.module
 */
@Component
@Data
public class ScreenModule {

    @Autowired
    private WordDao wordDao;
    @Autowired
    private NoteDao noteDao;
    @Autowired
    private MarkDao markDao;


    String MARK = "*";
    private boolean noChinese;


    public void render(WordEntity word) {
        if(word == null){
            System.out.println("NULL");;
            return;
        }
        String schema = "%s\n笔记:\n%s \n释义: %s";
        List<NoteEntity> notes = noteDao.selectByWordIdOrderByIndex(word.getId());
        StringBuilder noteStr = new StringBuilder();
        for (NoteEntity note : notes) {
            noteStr.append("    ").append(note.getIndex()).append(": ").append(note.toString()).append("\n");
        }

        StringBuilder wordValue = new StringBuilder(word.getValue());
        List<MarkEntity> marks = markDao.selectByWordIdOrderByIndex(word.getId());
        if (!CollectionUtils.isEmpty(marks)) {
            for (int i = 0; i < marks.size(); i++) {
                wordValue.append(" ").append(MARK);
            }
        }
        System.out.println(String.format(schema, wordValue, noteStr, noChinese ? "" : word.getAnnotation()));
    }

    public void render(String s) {
        System.out.println(s);
    }

    public void renderTodayProgress(List<WordEntity> todayProgress) {
        render("\n你今天完成了这些单词:");
        for (WordEntity word : todayProgress) {
            render(String.format("%s", word.getValue()));
        }
        render(String.format("\nToday's progress: %s 个单词\n", todayProgress.size()));
    }


}
