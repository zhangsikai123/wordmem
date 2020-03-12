package com.sky.wordmem.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/24/20
 * @description com.sky.wordmem.utils
 */
@Slf4j
@Component
public class RequestUtil {

    static final String RESPONSE_SCHEMA = "{data:%s}";
    static final String RESPONSE__LIST_SCHEMA = "{data:%s}";

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    WebClient webClient;
    /**
     * description
     * deprecated
     *
     * @return null if status not equals to 200, else the response body
     * @date 2/25/20 8:55 AM
     */
    @Deprecated
    public JSONObject get(String url) {

        String resBody = restTemplate.getForObject(url,  String.class);

        JSONObject ret;
        try {
            ret = JSON.parseObject(String.format(RESPONSE_SCHEMA, resBody));
        }catch(JSONException ignored){
            ret = JSON.parseObject(String.format(RESPONSE__LIST_SCHEMA, resBody));
        }
        return ret;
    }

    public JSONObject get2(String url) {
        WebClient.ResponseSpec res = webClient.get().uri(url).retrieve();
        JSONObject ret;
        String resBody = res.bodyToMono(String.class).block();
        try {
            ret = JSON.parseObject(String.format(RESPONSE_SCHEMA, resBody));
        }catch(JSONException ignored){
            ret = JSON.parseObject(String.format(RESPONSE__LIST_SCHEMA, resBody));
        }
        return ret;
    }
}
