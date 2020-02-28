package com.sky.wordmem.dao;

import com.sky.wordmem.entity.NoteEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/24/20
 * @description com.sky.wordmem.dao
 */
@Mapper
@Component
public interface NoteDao {
    // 插入 并查询id 赋给传入的对象
    @Insert("INSERT INTO notes(value, word_id, 'index', create_time, update_time) VALUES(#{value}, #{wordId}, #{index}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys=true)
    int insert(NoteEntity model);

    // 根据 ID 查询
    @Select("SELECT * FROM notes WHERE id=#{id}")
    NoteEntity select(Long id);

    // 查询全部
    @Select("SELECT * FROM notes")
    List<NoteEntity> selectAll();

    // 根据wordId查询全部
    @Select("SELECT * FROM notes where word_id=#{wordId} order by 'index'")
    List<NoteEntity> selectByWordIdOrderByIndex(Long wordId);

    // 根据日期查询  [a, b]
    @Select("SELECT * FROM notes where update_time between #{begin} and #{end}")
    List<NoteEntity> selectByDatetime(long begin, long end);

//    // 根据日期查询 [a, b)
//    @Select("SELECT * FROM notes where update_time between #{begin} and #{end}")
//    List<NoteEntity> selectByDate(Date begin, Date end);

    // 更新 value
    @Update("UPDATE notes SET value=#{value} WHERE id=#{id}")
    int updateValue(NoteEntity model);

    // 根据 ID 删除
    @Delete("DELETE FROM notes WHERE id=#{id}")
    int delete(Long id);
}
