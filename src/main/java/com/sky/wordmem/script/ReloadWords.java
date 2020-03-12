package com.sky.wordmem.script;

import com.sky.wordmem.dao.WordDao;
import com.sky.wordmem.entity.WordEntity;
import com.sky.wordmem.utils.ExcelUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.script
 */
@Component
@Data
@Slf4j
public class ReloadWords implements Runnable {
    /**
     * default importData
     **/
    private String fileAddress = "importData.xlsx";
    @Autowired
    WordDao wordDao;

    @Override
    public void run() {
        wordDao.deleteAll();
        try {
            ClassPathResource resource = new ClassPathResource(fileAddress);
            InputStream fileInput = resource.getInputStream();
            List<List<String>> sheet = ExcelUtil.readExcelFromFile(fileInput, 0);
            int count = 0;
            int total = sheet.size();
            for (List<String> row : sheet) {
                String value = row.get(0);
                String annotation = row.get(1);
                WordEntity data = WordEntity.builder().annotation(annotation.trim()).value(value.trim()).build();
                wordDao.insert(data);
                log.info(String.format("%s/%s", count, total));
                count++;
            }
        } catch (IOException e) {
            log.error(e.toString());
        }
    }
}
