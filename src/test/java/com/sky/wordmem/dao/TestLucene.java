package com.sky.wordmem.dao;

import com.sky.UnitTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;


/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/8/20
 * @description com.sky.wordmem.singleton
 */
@Slf4j
public class TestLucene extends UnitTest {


    @Test
    public void testLucene() throws IOException {
        lucene.getIndexWriter().deleteAll();
        lucene.getIndexWriter().commit();
        lucene.refreshSearcher();

        String q = "test4";
        TopDocs hits = lucene.getIndexSearcher().search(lucene.buildQuery("f1", q), 1);
        assert hits.totalHits.value == 0;
        Document doc = new Document();
        doc.add(new TextField("f1", q + " is watching u", Field.Store.YES));
        doc.add(new StringField("id", UUID.randomUUID().toString(), Field.Store.YES));
        lucene.getIndexWriter().addDocument(doc);
        lucene.getIndexWriter().commit();
        lucene.refreshSearcher();
        hits = lucene.getIndexSearcher().search(lucene.buildQuery("f1", q), 1);
        String id = lucene.getIndexSearcher().doc(hits.scoreDocs[0].doc).get("id");
        assert hits.totalHits.value == 1;
        lucene.getIndexWriter().deleteDocuments(new Term("id", id));
        lucene.getIndexWriter().commit();
        lucene.refreshSearcher();
        hits = lucene.getIndexSearcher().search(lucene.buildQuery("f1", q), 1);
        assert hits.totalHits.value == 0;
    }
//    Object[] idBox = new Object[]{null};
//    boolean[] resultBox = new boolean[]{true};
//    int threadNum = 100;
//
//    class TestThread extends Thread {
//

//        @Override
//        public void run() {
//            Lucene l = Lucene.getInstance();
//            if (idBox[0] == null) {
//                idBox[0] = l.hashCode();
//            } else {
//                resultBox[0] = idBox[0].equals(l.hashCode());
//            }
//        }
//    }
//
//
//    @Test
//    public void test() throws InterruptedException {
//        TestThread[] ts = new TestThread[threadNum];
//        for (int i = 0; i < threadNum; i++) {
//            ts[i] = new TestThread();
//        }
//        for(TestThread t: ts){
//            t.start();
//        }
//        for(TestThread t: ts){
//            t.join();
//        }
//        assert resultBox[0];
//    }
}
