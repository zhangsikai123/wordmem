package com.sky.wordmem.script;

import com.sky.wordmem.dao.WordDao;
import com.sky.wordmem.entity.WordEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/12/20
 * @description com.sky.wordmem.script
 */
@Component
@Slf4j
public class PurgeData implements Runnable {
    @Autowired
    WordDao wordDao;

    @Override
    public void run() {
        int count = 0;
        List<WordEntity> data = wordDao.selectAll();
        int total = data.size();
        for (WordEntity word : data) {
            word.setValue(word.getValue().replace("\uF020", "").trim());
            wordDao.updateValue(word);
            log.info(String.format("progress %s/%s", count++, total));

        }
    }
}
