package com.sky.wordmem.dao;

import com.sky.wordmem.entity.WordEntity;
import com.sky.wordmem.extensions.myBatis.SimpleSelectInExtendedLanguageDriver;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.dao
 */
@Mapper
@Component
public interface WordDao {
    @Insert("INSERT INTO words(value, annotation, create_time, update_time) VALUES(#{value}, #{annotation}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true)
    void insert(WordEntity model);

    // 根据 ID 查询
    @Select("SELECT * FROM words WHERE id=#{id}")
    WordEntity select(Long id);

    // 查询全部
    @Select({"select * FROM words WHERE  id IN (#{list})"})
    @Lang(SimpleSelectInExtendedLanguageDriver.class)
    List<WordEntity> multiSelect(@Param("list") List<Long> list);

    // 查询全部
    @Select("SELECT * FROM words")
    List<WordEntity> selectAll();

    @Select("SELECT * FROM words order by id")
    List<WordEntity> selectAllOrdered();

    // 更新 value
    @Update("UPDATE words SET value=#{value} WHERE id=#{id}")
    int updateValue(WordEntity model);

    // 根据 ID 删除
    @Delete("DELETE FROM words WHERE id=#{id}")
    int delete(Long id);

    @Delete("DELETE FROM words")
    int deleteAll();
}
