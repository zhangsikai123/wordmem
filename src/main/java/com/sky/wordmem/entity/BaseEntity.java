package com.sky.wordmem.entity;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.entity
 */
import com.sky.wordmem.annotation.CreatedAt;
import com.sky.wordmem.annotation.UpdatedAt;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Data
abstract class BaseEntity implements Serializable{

    private Long id;

    @CreatedAt
    private Date createTime;
    @UpdatedAt
    private Date updateTime;
}