package com.sky.wordmem.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

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
    /**
     * description
     *
     *
     * @return null if status not equals to 200, else the response body
     * @date 2/25/20 8:55 AM
     */
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
}
