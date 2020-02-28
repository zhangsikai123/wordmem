package com.sky.wordmem.util;

import com.alibaba.fastjson.JSONObject;
import com.sky.UnitTest;
import com.sky.wordmem.adaptor.dictAdaptor.MarriamWebster;
import com.sky.wordmem.adaptor.dictAdaptor.vo.SearchResponse;
import com.sky.wordmem.utils.RequestUtil;
import org.junit.Test;

import java.util.Scanner;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/27/20
 * @description com.sky.wordmem.util
 */
public class RequestUtilTest extends UnitTest {


    @Test
    public void test() {
        Scanner scanner = new Scanner(System.in);
        SearchResponse response = marriamWebster.searchWord("word");
        marriamWebster.render(response);
    }
}
