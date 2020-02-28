package com.sky.wordmem.entity;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/28/20
 * @description com.sky.wordmem.entity 不记得的次数，外键word id
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkEntity extends BaseEntity{
    @NotNull
    private long wordId;
}
