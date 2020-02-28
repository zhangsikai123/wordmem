package com.sky.wordmem.adaptor.dictAdaptor.vo;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/27/20
 * @description com.sky.wordmem.adaptor.dictAdaptor.vo
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class DictWord {


    @NotNull
    private String word;

    /**
     * PART OF SPEECH: adj, n, v ..
     **/
    @NotNull
    private String pos;

    /**
     * 暂时没有用
     **/
    List<Sense> senses;

    @NotNull
    private List<String> stems;

    List<String> shortDef;

    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Sense {
        List<Def> defs;
    }

    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Def {
        String definition;
        String example;
    }
}
