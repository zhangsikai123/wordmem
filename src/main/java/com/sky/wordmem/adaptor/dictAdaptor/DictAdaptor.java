package com.sky.wordmem.adaptor.dictAdaptor;

import com.sky.wordmem.adaptor.dictAdaptor.vo.DictWord;
import com.sky.wordmem.adaptor.dictAdaptor.vo.SearchResponse;

import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/27/20
 * @description com.sky.wordmem.adaptor.dictAdaptor
 */
public interface DictAdaptor {

    SearchResponse searchWord(String query);

    void render(SearchResponse response);
}

