package com.sky.wordmem.adaptor.dictAdaptor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.sky.wordmem.adaptor.dictAdaptor.vo.DictWord;
import com.sky.wordmem.adaptor.dictAdaptor.vo.SearchResponse;
import com.sky.wordmem.module.ScreenModule;
import com.sky.wordmem.utils.RequestUtil;
import javafx.stage.Screen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/27/20
 * @description com.sky.wordmem.adaptor.dictAdaptor
 */
@Component
public class MarriamWebster implements DictAdaptor {

    static String KEY = "8c21257f-ffa2-4ef3-a912-026676f245b8";
    static String COLLEGIATE = "https://dictionaryapi.com/api/v3/references/collegiate/json";
    static String SCHEMA = "%s/%s?key=%s";

    @Autowired
    ScreenModule screen;
    @Autowired
    RequestUtil requestUtil;

    @Override
    public SearchResponse searchWord(String query) {
        String url = String.format(SCHEMA, COLLEGIATE, query, KEY);
        JSONArray response = requestUtil.get(url).getJSONArray("data");
        SearchResponse resBuilder = new SearchResponse();

        // 找第一个策略
        JSONObject target;
        try {
            target = response.getJSONObject(0);
        } catch (ClassCastException ignored) {
            String guess = response.getString(0);
            resBuilder.setStatus(0);
            resBuilder.setBestGuesses(response.toJavaList(String.class));
            return resBuilder;
        }
        JSONObject meta = target.getJSONObject("meta");
        String word = meta.getString("id");
        JSONArray stems = meta.getJSONArray("stems");
        // PART OF STRING
        String pos = target.getString("fl");
        List<String> shortDefs = target.getJSONArray("shortdef").toJavaList(String.class);
//        List<DictWord.Sense> senses = new ArrayList<>();
//        if (!defs.isEmpty()) {
//            JSONArray sseq = defs.getJSONObject(0).getJSONArray("sseq");
//            for (int i = 0; i < sseq.size(); i++) {
//                JSONArray defList = sseq.getJSONArray(i);
//                DictWord.Sense sense = new DictWord.Sense();
//                ArrayList<DictWord.Def> senseDefs = new ArrayList<>();
//                sense.setDefs(senseDefs);
//                for (int j = 0; j < defList.size(); j++) {
//                    JSONArray defBody = defList.getJSONArray(j);
//                    String definition = "";
//                    String example = "";
//                    if("sen".equals(defBody.getString(0))){
//                        JSONArray sls = defBody.getJSONObject(1).getJSONArray("sls");
//                        definition = sls.getString(0);
//                        example = "";
//                    }else if("sense".equals(defBody.getString(0))) {
//                        JSONArray dt = defBody.getJSONObject(1).getJSONArray("dt");
//                        definition = dt.getJSONArray(0).getString(1);
//                        example = dt.getJSONArray(1).getJSONArray(1).getJSONObject(0).getString("t");
//                    }
//                    senseDefs.add(DictWord.Def.builder()
//                            .definition(definition)
//                            .example(example)
//                            .build());
//                }
//                senses.add(sense);
//            }
//        }
        DictWord dictWord = DictWord.builder().word(word)
                .pos(pos)
                .shortDef(shortDefs)
                .stems(stems.toJavaList(String.class))

                .build();
        resBuilder.setStatus(1);
        resBuilder.setWord(dictWord);
        return resBuilder;
    }

    @Override
    public void render(SearchResponse response) {
        String SCHEMA_BEST_GUESSES = "妹找到...猜你想找:%s\n";
        String SCHEMA_BEST = "Stems:\n  %s\nPos:\n  %s\nSense:\n    %s\n";
        if (response.getStatus() == 0) {
            String words = String.join("\n", response.getBestGuesses());
            System.out.println(String.format(SCHEMA_BEST_GUESSES, words));
            return;
        }
        String senses = "";
        int count = 0;
        DictWord word = response.getWord();
        String stems = String.join(" | ", word.getStems());
        if (!CollectionUtils.isEmpty(word.getSenses())) {
            for (DictWord.Sense sense : word.getSenses()) {
                List<DictWord.Def> defs = sense.getDefs();
                for (DictWord.Def def : defs) {
                    String defString = String.format("%s: %s\n  %s", count, def.getDefinition(), def.getExample());
                    senses += defString + "\n";
                }
                count++;
            }
        } else {
            //用short defs
            senses = String.join("\n    ", word.getShortDef());
        }
        System.out.println(String.format(SCHEMA_BEST, stems, word.getPos(), senses));
    }
}
