package com.sky.wordmem.dao;

import com.sky.UnitTest;
import com.sky.wordmem.entity.NoteEntity;
import com.sky.wordmem.entity.TestEntity;
import com.sky.wordmem.entity.WordEntity;
import com.sky.wordmem.utils.DatetimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.dao
 */
@Slf4j
public class TestDaoTest extends UnitTest {


    @Test
    public void test(){
        TestEntity data = TestEntity.builder().f1("1").f2(2).f3(true).f4(new Date()).build();
        testDao.insert(data);
        Long id = data.getId();
        TestEntity testEntity = testDao.select(id);
        System.out.println(testEntity.getF1());
    }


    @Test
    public void testWordDaoCrud(){
        String ann = "第一个haha";
        WordEntity data = WordEntity.builder().annotation(ann).value("first").build();
        wordDao.insert(data);
        Long id = data.getId();
        WordEntity wordEntity = wordDao.select(id);
        List<WordEntity> wordEntities = wordDao.multiSelect(Collections.singletonList(id));
        assert !CollectionUtils.isEmpty(wordEntities);
        for(WordEntity word: wordEntities){
            assert word.getId().equals(id);
        }
        assert wordEntity.getAnnotation().equals(ann);
        wordDao.delete(id);
        wordEntity = wordDao.select(id);
        assert null == wordEntity;
    }

    @Test
    public void testNoteDaoCrud(){

        String ann = "第一个haha";
        WordEntity data = WordEntity.builder().annotation(ann).value("first").build();
        wordDao.insert(data);
        NoteEntity note = NoteEntity.builder().wordId(data.getId()).index(1).value(ann).build();
        noteDao.insert(note);
        Long id = note.getId();
        NoteEntity noteEntity = noteDao.select(id);
        assert noteEntity.getValue().equals(ann);

        Date now = new Date();
        Date end = now;
        Date begin = DateUtils.addMinutes(now, -1);
        List<NoteEntity> notes = noteDao.selectByDatetime(begin.getTime(), end.getTime());
        for(NoteEntity n: notes){
            assert DatetimeUtil.dateToString(
                    n.getUpdateTime()).equals(DatetimeUtil.dateToString(new Date()));
        }
        noteDao.delete(id);
        noteEntity = noteDao.select(id);
        assert null == noteEntity;
    }

    @Test
    public void testMetaDao(){
        String a = metaDao.getLastCursor().get(0);
        int v = Integer.parseInt(a) + 1;
        metaDao.preserveCursor(v);
        assert Integer.parseInt(metaDao.getLastCursor().get(0)) == v;
    }

    @Test
    public void testReindexNotes(){
//        reindexNotes.run();
//        List<WordEntity> res = notebook.searchNotes("haha");
//        log.info(String.format("the words with first in it:  %s",
//                res.stream().map(WordEntity::getValue).collect(Collectors.toList())));
    }
}
