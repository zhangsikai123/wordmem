package com.sky.wordmem.dao;

import com.sky.wordmem.entity.TestEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.dao
 */
@Mapper
@Component
public interface TestDao {
    // 插入 并查询id 赋给传入的对象
    @Insert("INSERT INTO tests(f1, f2, f3, f4, create_time, update_time) VALUES(#{f1}, #{f2}, #{f3}, #{f4}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys=true)
    int insert(TestEntity model);

    // 根据 ID 查询
    @Select("SELECT * FROM tests WHERE id=#{id}")
    TestEntity select(Long id);

    // 查询全部
    @Select("SELECT * FROM tests")
    List<TestEntity> selectAll();

    // 更新 value
    @Update("UPDATE tests SET value=#{value} WHERE id=#{id}")
    int updateValue(TestEntity model);

    // 根据 ID 删除
    @Delete("DELETE FROM tests WHERE id=#{id}")
    int delete(Long id);
}
