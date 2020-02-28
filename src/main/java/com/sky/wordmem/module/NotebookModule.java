package com.sky.wordmem.module;

import com.sky.wordmem.dao.NoteDao;
import com.sky.wordmem.dao.WordDao;
import com.sky.wordmem.entity.NoteEntity;
import com.sky.wordmem.entity.WordEntity;
import com.sky.wordmem.utils.CommonUtil;
import com.sky.wordmem.utils.DatetimeUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/24/20
 * @description com.sky.wordmem.module
 */
@Component
public class NotebookModule {

    @Autowired
    WordDao wordDao;
    @Autowired
    NoteDao noteDao;

    public int writeNoteUnderWord(WordEntity word, String note) {
        Long wordId = word.getId();
        List<NoteEntity> notes = noteDao.selectByWordIdOrderByIndex(wordId);
        int index = notes.size();
        return noteDao.insert(
                NoteEntity.builder().wordId(wordId).value(note).index(index).build());
    }

    public Long deleteNoteUnterWord(WordEntity word, int index){
        Long wordId = word.getId();
        List<NoteEntity> notes = noteDao.selectByWordIdOrderByIndex(wordId);
        Long deletedId = null;
        for(NoteEntity note: notes){
            if(note.getIndex().equals(index)){
                noteDao.delete(note.getId());
                deletedId = note.getId();
            }
        }
        return deletedId;
    }

    public List<NoteEntity> todayNotes() {
        Date today = new Date();
        return noteDao.selectByDatetime(DatetimeUtil.dateToUtcUntilDay(today).getTime(),
                DatetimeUtil.dateToUtcUntilDay(DateUtils.addDays(today, 1)).getTime());
    }

    /**
     * 今天记了多少个单词
     **/
    public List<WordEntity> todayProgress() {
        List<Long> data = todayNotes()
                .stream()
                .map(NoteEntity::getWordId)
                .distinct()
                .collect(Collectors.toList());
        return CommonUtil.ifNullThen(wordDao.multiSelect(data),new ArrayList<>());
    }

    /**
     * 每分钟多少个
     **/
    public long countSpeed() {
        // unit: Minute
        int interval = 1;
        Date end = new Date();
        Date begin = DateUtils.addMinutes(end, -1 * interval);
        return noteDao.selectByDatetime(begin.getTime(), end.getTime())
                .stream()
                .map(NoteEntity::getWordId)
                .distinct()
                .count();
    }
}
