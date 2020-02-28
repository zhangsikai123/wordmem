package com.sky.wordmem.dao;

import com.sky.wordmem.entity.MarkEntity;
import com.sky.wordmem.entity.NoteEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/28/20
 * @description com.sky.wordmem.dao
 */
@Mapper
@Component
public interface MarkDao {

    // 根据wordId查询全部
    @Select("SELECT * FROM marks where word_id=#{wordId} order by 'index'")
    List<MarkEntity> selectByWordIdOrderByIndex(Long wordId);

    @Insert("INSERT INTO marks(word_id, create_time, update_time) VALUES(#{wordId}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys=true)
    int insert(MarkEntity mark);
}
