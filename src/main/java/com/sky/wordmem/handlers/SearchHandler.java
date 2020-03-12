package com.sky.wordmem.handlers;

import com.sky.wordmem.Request;
import com.sky.wordmem.context.BaseContext;
import com.sky.wordmem.entity.WordEntity;
import com.sky.wordmem.module.NotebookModule;
import com.sky.wordmem.module.ScreenModule;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/2/20
 * @description com.sky.wordmem.handlers
 */
@Command({"s", "search"})
@Component
public class SearchHandler implements Handler{

    @Autowired
    NotebookModule notebook;
    @Autowired
    ScreenModule screen;

    @Override
    public void handle(BaseContext cxt, Request request) {
        //trim "s "
        String param = request.getArg();
        List<WordEntity> res = notebook.searchNotes(param);
        screen.render(Strings.join(res.stream().map(WordEntity::getValue)
                        .collect(Collectors.toList()), '\n'));
    }
}
