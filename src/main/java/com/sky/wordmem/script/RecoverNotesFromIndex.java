package com.sky.wordmem.script;

import com.sky.wordmem.config.Lucene;
import com.sky.wordmem.dao.WordDao;
import com.sky.wordmem.entity.WordEntity;
import com.sky.wordmem.module.NotebookModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/9/20
 * @description com.sky.wordmem.script
 */
@Component
@Slf4j
public class RecoverNotesFromIndex implements Runnable{
    @Autowired
    Lucene lucene;
    @Autowired
    NotebookModule notebookModule;

    @Override
    public void run() {
        TopDocs hits = null;
        try {
            hits = lucene.getIndexSearcher().search(lucene.buildQuery("note", "*:*"), 5000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int count = 0;
        int total = hits.scoreDocs.length;
        for (int i = 0; i < hits.scoreDocs.length; i++) {
            try {
                Document doc = lucene.getIndexSearcher().doc(hits.scoreDocs[i].doc);
                notebookModule.writeNoteUnderWord(Long.parseLong(doc.get("wordId")), doc.get("note"));
                log.info(String.format("note indexing progress %s/%s", count++, total));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
