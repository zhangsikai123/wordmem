package com.sky.wordmem.script;

import com.sky.wordmem.config.Lucene;
import com.sky.wordmem.dao.NoteDao;
import com.sky.wordmem.dao.NoteIndex;
import com.sky.wordmem.entity.NoteEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/6/20
 * @description com.sky.wordmem.script
 */
@Component
@Data
@Slf4j
public class ReindexNotes implements Runnable {

    @Autowired
    NoteDao noteDao;
    @Autowired
    NoteIndex noteIndex;
    @Autowired
    Lucene lucene;

    @Override
    public void run() {
        int count = 0;
        List<NoteEntity> notes = noteDao.selectAll();
        int total = notes.size();
        for (NoteEntity note : notes) {
            noteIndex.insertWithoutCommit(note);
            log.info(String.format("note indexing progress %s/%s", count++, total));
        }
        try {
            lucene.getIndexWriter().commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

