package com.sky.wordmem.util;
import com.sky.UnitTest;
import com.sky.wordmem.adaptor.dictAdaptor.vo.SearchResponse;
import org.junit.Test;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/27/20
 * @description com.sky.wordmem.util
 */
public class RequestUtilTest extends UnitTest {


    @Test
    public void test() {
//        JSONObject ret = requestUtil.get2("https://www.baidu.com");
//        System.out.println(ret);
        SearchResponse response = marriamWebster.searchWord("word");
        marriamWebster.render(response);
    }
}
