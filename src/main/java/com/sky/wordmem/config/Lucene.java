package com.sky.wordmem.config;

import com.sky.wordmem.Exceptions;
import lombok.Data;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/6/20
 * @description com.sky.wordmem.config
 */
@Configuration
@Data
public class Lucene implements InitializingBean {

    @Value("${index.indexPath:}")
    private String indexPath;

    private IndexWriter indexWriter;
    private IndexSearcher indexSearcher;
    private Analyzer analyzer;

    public void init(){
        intiAnalyzer();
        initWriter();
        initSearcher();
    }

    private void intiAnalyzer() {
        analyzer = new StandardAnalyzer();
    }

    public void initWriter() {
        try {
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriter = new IndexWriter(dir, iwc);
        } catch (IOException e) {
            throw Exceptions.luceneException(String.format("init writer error %s", e.toString()));
        }
    }

    public void initSearcher() {
        DirectoryReader reader;
        try {
            reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            indexSearcher = new IndexSearcher(reader);
        } catch (IOException e) {
            throw Exceptions.luceneException(String.format("init searcher error %s", e.toString()));
        }
    }

    public void refreshSearcher(){
        DirectoryReader reader;
        IndexReader oldReader = indexSearcher.getIndexReader();
        try {
            reader = DirectoryReader.openIfChanged(
                    (DirectoryReader) indexSearcher.getIndexReader());
            if(reader!=null) {
                oldReader.close();
                indexSearcher = new IndexSearcher(reader);
            }
        } catch (IOException e) {
            throw Exceptions.luceneException(String.format("refresh searcher error %s", e.toString()));
        }
    }

    public Query buildQuery(String field, String query) {

        QueryParser parser = new QueryParser(field, analyzer);

        try {
            return parser.parse(query);
        } catch (ParseException e) {
            throw Exceptions.luceneException(String.format("illegal query %s:", query));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
