package com.sky.wordmem.config;

import com.sky.wordmem.interceptors.InterceptorManager;
import com.sky.wordmem.interceptors.MybatisUpdateInterceptor;
import com.sky.wordmem.interceptors.SqlQueryInterceptor;
import com.sky.wordmem.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.config
 */
@Configuration
@Slf4j
public class Mybatis {


    @Autowired
    private DataSource dataSource;
    @Autowired
    private InterceptorManager interceptorManager;

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        List<Interceptor> interceptors = interceptorManager.getMyBatisPlugins();
        log.info("mybatis plugins: " + interceptors.toString());
        sqlSessionFactoryBean.setPlugins(interceptors.toArray(new Interceptor[interceptors.size()]));
        org.apache.ibatis.session.Configuration conf =
                new org.apache.ibatis.session.Configuration();
        conf.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(conf);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        //com.example.demo.dal.mapper 这个包名是所有的Mapper.java文件所在的路径，该包下面的子包里面的文件同样会扫描到。
        //此包名与具体的应用的名称相关
        mapperScannerConfigurer.setBasePackage("com.sky.wordmem.dao");
        return mapperScannerConfigurer;
    }
}
