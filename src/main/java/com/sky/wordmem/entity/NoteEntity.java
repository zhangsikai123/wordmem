package com.sky.wordmem.entity;

import lombok.*;
import javax.validation.constraints.NotNull;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/24/20
 * @description com.sky.wordmem.entity
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteEntity extends BaseEntity{
    // wordId + index  unique

    private String value;

    @NotNull
    private Long wordId;

    @NotNull
    private Integer index;

    @Override
    public String toString(){
        return value;
    }
}
