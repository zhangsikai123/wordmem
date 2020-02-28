package com.sky.wordmem.interceptors;

import lombok.Data;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/25/20
 * @description com.sky.wordmem.interceptors
 */
@Data
@Component
public class InterceptorManager {

    private List<Interceptor> myBatisPlugins = new ArrayList<>();

    @Autowired
    MybatisUpdateInterceptor mybatisUpdateInterceptor;
    @Autowired(required = false)
    SqlQueryInterceptor sqlQueryInterceptor;

    public void registerMybatisPlugins(Interceptor plugin){
        myBatisPlugins.add(plugin);
    }
}
