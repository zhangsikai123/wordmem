package com.sky.wordmem.interceptors;

import com.sky.wordmem.annotation.CreatedAt;
import com.sky.wordmem.annotation.UpdatedAt;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.Properties;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/24/20
 * @description com.sky.wordmem.interceptors
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
@Component
@Slf4j
public class MybatisUpdateInterceptor implements Interceptor, InitializingBean {

    @Autowired
    InterceptorManager interceptorManager;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        boolean createAt = false;
        boolean updateAt = false;

        if (SqlCommandType.INSERT.equals(sqlCommandType)) {
            createAt = true;
        }
        if (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType)) {
            updateAt = true;
        }
        // 既不是insert 也不是update
        if (!createAt && !updateAt) {
            return invocation.proceed();
        }

        Object parameter = invocation.getArgs()[1];
        Class<?> clazz = parameter.getClass();

        while (clazz != null) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.getAnnotation(CreatedAt.class) != null) {
                    // insert 语句插入 createTime
                    if (createAt) {
                        field.setAccessible(true);
                        field.set(parameter, new Date());
                    }
                }
                // insert 或 update 语句插入 updateTime
                if (field.getAnnotation(UpdatedAt.class) != null) {
                    if (updateAt) {
                        field.setAccessible(true);
                        field.set(parameter, new Date());
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        interceptorManager.registerMybatisPlugins(this);
    }
}
