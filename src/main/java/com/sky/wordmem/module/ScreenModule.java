package com.sky.wordmem.module;

import com.sky.wordmem.dao.NoteDao;
import com.sky.wordmem.dao.WordDao;
import com.sky.wordmem.entity.NoteEntity;
import com.sky.wordmem.entity.WordEntity;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private boolean noChinese;

    public void render(WordEntity word) {
        String schema = "%s\n释义: %s\n笔记:\n%s";
        List<NoteEntity> notes = noteDao.selectByWordIdOrderByIndex(word.getId());
        StringBuilder noteStr = new StringBuilder();
        int indexView = 0;
        for (NoteEntity note : notes) {
            noteStr.append("    ").append(indexView).append(": ").append(note.toString()).append("\n");
            indexView++;
        }
        System.out.println(String.format(schema, word.getValue(), noChinese ? "" : word.getAnnotation(), noteStr));
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
