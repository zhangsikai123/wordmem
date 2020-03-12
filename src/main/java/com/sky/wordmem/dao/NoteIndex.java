package com.sky.wordmem.dao;

import com.sky.wordmem.Exceptions;
import com.sky.wordmem.config.Lucene;
import com.sky.wordmem.entity.NoteEntity;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/6/20
 * @description com.sky.wordmem.dao
 */
@Component
public class NoteIndex {


    @Autowired
    private Lucene lucene;

    public List<Document> select(String note, int n) {
        IndexSearcher searcher = lucene.getIndexSearcher();
        try {
            TopDocs hits = searcher.search(lucene.buildQuery("note", note), n);
            return Arrays.stream(hits.scoreDocs).map(e -> {
                try {
                    return searcher.doc(e.doc);
                } catch (IOException ex) {
                    throw Exceptions.luceneException(
                            String.format("error during search %s", e.toString()));
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw Exceptions.luceneException(
                    String.format("error during search %s", e.toString()));
        }
    }

    public void insert(NoteEntity note) {
        insertWithoutCommit(note);
        try {
            lucene.getIndexWriter().commit();
        } catch (IOException e) {
            throw Exceptions.luceneException(
                    String.format("error during indexing %s", e.toString()));
        }
    }

    public void insertWithoutCommit(NoteEntity note) {
        try {
            Document doc = new Document();
            doc.add(new StringField("wordId", String.valueOf(note.getWordId()), Field.Store.YES));
            doc.add(new StringField("noteId", String.valueOf(note.getId()), Field.Store.YES));
            doc.add(new TextField("note", note.getValue(), Field.Store.YES));
            lucene.getIndexWriter().addDocument(doc);
        } catch (IOException e) {
            throw Exceptions.luceneException(
                    String.format("error during indexing %s", e.toString()));
        }
    }

    public void delete(Long noteId) {
        IndexWriter indexWriter = lucene.getIndexWriter();
        try {
            indexWriter.deleteDocuments(new Term("noteId", String.valueOf(noteId)));
            indexWriter.commit();
        } catch (IOException e) {
            throw Exceptions.luceneException(
                    String.format("error during delete %s", e.toString()));
        }
    }
}
