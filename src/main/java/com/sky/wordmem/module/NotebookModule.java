package com.sky.wordmem.module;

import com.sky.wordmem.Exceptions;
import com.sky.wordmem.config.Lucene;
import com.sky.wordmem.dao.MarkDao;
import com.sky.wordmem.dao.NoteDao;
import com.sky.wordmem.dao.NoteIndex;
import com.sky.wordmem.dao.WordDao;
import com.sky.wordmem.entity.MarkEntity;
import com.sky.wordmem.entity.NoteEntity;
import com.sky.wordmem.entity.WordEntity;
import com.sky.wordmem.utils.CommonUtil;
import com.sky.wordmem.utils.DatetimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
public class NotebookModule {

    @Autowired
    WordDao wordDao;
    @Autowired
    NoteDao noteDao;
    @Autowired
    private MarkDao markDao;
    @Autowired
    private NoteIndex noteIndex;


    /**
     * 根据笔记搜索单词
     *
     * @param query
     * @return java.util.List<com.sky.wordmem.entity.WordEntity>
     * @date 3/6/20 10:50 PM
     */
    public List<WordEntity> searchNotes(String query){
        if(StringUtils.isEmpty(query)){
            return new ArrayList<>();
        }
        int defaultLimit = 50;
        return wordDao.multiSelect(
                noteIndex.select(query, defaultLimit)
                        .stream()
                        .map(e->Long.parseLong(e.get("wordId")))
                        .distinct()
                        .collect(Collectors.toList()));

    }

    public Long writeNoteUnderWord(WordEntity word, String note) {
        Long wordId = word.getId();
        return writeNoteUnderWord(wordId, note);
    }

    public Long writeNoteUnderWord(Long wordId, String note) {
        List<NoteEntity> notes = noteDao.selectByWordIdOrderByIndex(wordId);
        int index = notes.size();
        NoteEntity noteEntity = NoteEntity.builder().wordId(wordId).value(note).index(index).build();
        Long id = noteDao.insert(noteEntity);
        noteIndex.insert(noteEntity);
        return id;
    }

    public Long deleteNoteUnderWord(WordEntity word, int index) {
        Long wordId = word.getId();
        List<NoteEntity> notes = noteDao.selectByWordIdOrderByIndex(wordId);
        Long deletedId = null;
        for (NoteEntity note : notes) {
            if (note.getIndex().equals(index)) {
                noteDao.delete(note.getId());
                deletedId = note.getId();
                break;
            }
        }
        if(deletedId != null) {
            noteIndex.delete(deletedId);
        }
        return deletedId;
    }

    public int markWord(WordEntity word) {
        Long wordId = word.getId();
        return markDao.insert(MarkEntity.builder().wordId(wordId).build());
    }

    public List<WordEntity> review() {
        // 找三天之前一共学习过的单词
        List<NoteEntity> res = new ArrayList<>();
        for (int i = 3; i > 0; i--) {
            res.addAll(nDaysBeforeNotes(i));
        }
        return wordDao.multiSelect(
                res.stream()
                        .map(NoteEntity::getWordId)
                        .distinct()
                        .collect(Collectors.toList()));
    }

    public List<NoteEntity> todayNotes() {
//        Date today = new Date();
//        return noteDao.selectByDatetime(DatetimeUtil.dateToUtcUntilDay(today).getTime(),
//                DatetimeUtil.dateToUtcUntilDay(DateUtils.addDays(today, 1)).getTime());
        return nDaysBeforeNotes(0);
    }

    public List<NoteEntity> nDaysBeforeNotes(int n) {
        // n 天之前的笔记
        Date today = new Date();
        Date targetDay = DateUtils.addDays(today, -1 * n);
        Date endDay = DateUtils.addDays(targetDay, 1);
        return noteDao.selectByDatetime(
                DatetimeUtil.dateToUtcUntilDay(targetDay).getTime(),
                DatetimeUtil.dateToUtcUntilDay(endDay).getTime());
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
        return CommonUtil.ifNullThen(wordDao.multiSelect(data), new ArrayList<>());
    }

    /**
     * 今天记了多少个单词
     **/
    public List<WordEntity> nDaysBeforeProgress(int n) {
        List<Long> data = nDaysBeforeNotes(n)
                .stream()
                .map(NoteEntity::getWordId)
                .distinct()
                .collect(Collectors.toList());
        return CommonUtil.ifNullThen(wordDao.multiSelect(data), new ArrayList<>());
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
