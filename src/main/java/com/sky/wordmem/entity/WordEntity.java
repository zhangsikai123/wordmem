package com.sky.wordmem.entity;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.entity
 */
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WordEntity extends BaseEntity{

    private String annotation;
    private String value;

    @Override
    public String toString(){
        String schema = "%s\n释义: %s\n";
        return String.format(schema, this.value, this.annotation);
    }
}
