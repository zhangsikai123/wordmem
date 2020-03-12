package com.sky.wordmem.module;

import com.sky.wordmem.adaptor.dictAdaptor.DictAdaptor;
import com.sky.wordmem.adaptor.dictAdaptor.vo.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executors;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/24/20
 * @description com.sky.wordmem.module
 */
@Component
public class DictionaryModule {

    @Autowired
    ScreenModule screen;
    @Resource
    DictAdaptor marriamWebster;

    public void searchAndRender(String word) {
        Executors.newSingleThreadExecutor().submit(()->{
            SearchResponse response = marriamWebster.searchWord(word);
            screen.render("==============================================");
            marriamWebster.render(response);
            screen.render("==============================================");
        });
    }
}
