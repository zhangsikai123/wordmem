package com.sky.wordmem.dao;

import com.sky.wordmem.utils.ReadWriteUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/9/20
 * @description com.sky.wordmem.dao
 */
@Mapper
@Component
public interface MetaDao {

    @Select("select value from meta where name == 'cursor' LIMIT 1")
    @Options(keyColumn = "cursor")
    List<String> getLastCursor();

    @Update("update meta set value = #{cursor} where name == 'cursor'")
    void preserveCursor(int cursor);
}
