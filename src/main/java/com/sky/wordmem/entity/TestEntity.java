package com.sky.wordmem.entity;
import java.util.Date;

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
public class TestEntity extends BaseEntity {

    private String f1;
    private Integer f2;
    private Boolean f3;
    private Date f4;
}
