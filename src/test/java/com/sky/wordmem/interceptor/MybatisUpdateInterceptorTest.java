package com.sky.wordmem.interceptor;

import com.sky.UnitTest;
import com.sky.wordmem.entity.TestEntity;
import org.junit.Test;

import java.util.Date;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/24/20
 * @description com.sky.wordmem.interceptor
 */
public class MybatisUpdateInterceptorTest extends UnitTest {

    @Test
    public void testCreatedAndUpdatedAt() {
        TestEntity data = TestEntity.builder().f1("1").f2(2).f3(true).f4(new Date()).build();
        testDao.insert(data);
        assert null != data.getCreateTime() && null != data.getUpdateTime();
    }
}
