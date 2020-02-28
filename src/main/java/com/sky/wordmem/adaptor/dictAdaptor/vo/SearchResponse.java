package com.sky.wordmem.adaptor.dictAdaptor.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/27/20
 * @description com.sky.wordmem.adaptor.dictAdaptor
 */
@Data
public class SearchResponse {
    // 0: not found, 1: success
    @NotNull
    private Integer status;
    DictWord word;
    List<String> bestGuesses;
}
