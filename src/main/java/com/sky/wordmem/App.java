package com.sky.wordmem;

import com.sky.wordmem.config.Lucene;
import com.sky.wordmem.context.ContextManager;
import com.sky.wordmem.dao.WordDao;
import com.sky.wordmem.entity.WordEntity;
import com.sky.wordmem.module.DictionaryModule;
import com.sky.wordmem.module.NotebookModule;
import com.sky.wordmem.module.ScreenModule;
import com.sky.wordmem.script.PurgeData;
import com.sky.wordmem.script.RecoverNotesFromIndex;
import com.sky.wordmem.script.ReindexNotes;
import com.sky.wordmem.script.ReloadWords;
import com.sky.wordmem.utils.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.*;

import static com.sky.wordmem.utils.SearchUtil.searchObjectInList;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description PACKAGE_NAME
 */
@SpringBootApplication(scanBasePackages = {
        "com.sky.wordmem"
})
public class App implements CommandLineRunner {

    @Autowired
    NotebookModule notbook;
    @Autowired
    ReindexNotes reindexNotes;
    @Autowired
    Lucene lucene;
    @Autowired
    private WordDao wordDao;
    @Autowired
    private RecoverNotesFromIndex recoverNotesFromIndex;
    @Autowired
    private ReloadWords reloadWords;
    @Autowired
    private PurgeData purgeData;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run();
    }

//    @Override
//    public void run(String... args) throws Exception {
//        purgeData.run();
//    }

    @Override
    public void run(String... args) throws Exception {
        List<String> profiles =
                Arrays.asList(SpringContextUtil.getEnv().getActiveProfiles());
        if (profiles.contains("test")) {
            return;
        }
        WordDao wordDao = SpringContextUtil.getBean("wordDao");
        ScreenModule screen = SpringContextUtil.getBean("screenModule");
        NotebookModule notebook = SpringContextUtil.getBean("notebookModule");
        DictionaryModule dict = SpringContextUtil.getBean("dictionaryModule");
        CommandService commandService = SpringContextUtil.getBean("commandService");
        ContextManager cm = SpringContextUtil.getBean("contextManager");
        List<WordEntity> words = wordDao.selectAllOrdered();

        commandService.init();
        cm.init();

        while (true) {
            Request request = cm.context().nextCommand();
            try {
                commandService.service(request);
            } catch (Exception ex) {
                String s = request.getCommand();
                String arg = request.getArg();
                if ("".equals(s)) {
                    //NEXT
                    cm.context().addCursor();
                    screen.render(words.get(cm.context().getCursor()));
                } else if ("exit".equals(s)) {
                    cm.close();
                    screen.render("谢谢使用，下次再见，白衣白衣~");
                    return;
                } else if ("b".equals(s)) {
                    if (cm.context().getHistory().size() > 0) {
                        cm.context().setCursor(cm.context().getHistory().pollLast());
                        cm.context().renderCurrentWord();
                    } else {
                        screen.render("最后一个了");
                    }
                } else if ("n".equals(s)) {
                    String note = arg;
                    notebook.writeNoteUnderWord(cm.context().currentWord(), note);
                    cm.context().renderCurrentWord();
                } else if ("dn".equals(s)) {
                    WordEntity word = cm.context().currentWord();
                    try {
                        int index = Integer.parseInt(arg);
                        notebook.deleteNoteUnderWord(word, index);
                        screen.render(word);
                    } catch (NumberFormatException e) {
                        screen.render("dn only takes number as parameter, please re-input");
                    }
                } else if ("progress".equals(s)) {
                    screen.render(String.format("%s/%s", cm.context().getCursor(), cm.context().getValues().size()));
                }
//                else if ("today".equals(s)) {
//                    List<WordEntity> todayProgress = notebook.todayProgress();
//                    screen.renderTodayProgress(todayProgress);
//                    if (!CollectionUtils.isEmpty(todayProgress)) {
//                        cm.context().setForceCommand(todayProgress.get(todayProgress.size() - 1).getValue());
//                    }
//                }
//                else if ("no chinese".equals(s)) {
//                    screen.setNoChinese(true);
//                    wcxt.renderCurrentWord();
//                } else if ("show chinese".equals(s)) {
//                    screen.setNoChinese(false);
//                    wcxt.renderCurrentWord();
//                }
                else if ("fw".equals(s)) {
                    String query;
                    if (!StringUtils.isEmpty(arg)) {
                        query = arg;
                        dict.searchAndRender(query);
                    } else {
                        query = cm.context().currentWord().getValue();
                        dict.searchAndRender(query);
                    }
                } else if ("m".equals(s)) {
                    WordEntity word = cm.context().currentWord();
                    notebook.markWord(word);
                    screen.render(word);
                } else {
                    //搜索某个词
                    cm.context().getHistory().add(cm.context().getCursor());
                    cm.context().setCursor(searchObjectInList(cm.context().getValues(), s));
                    if (cm.context().getCursor() < 0) {
                        //没找到，回滚刚刚的操作
                        if (cm.context().getHistory().size() > 0) {
                            cm.context().setCursor(cm.context().getHistory().pollLast());
                        }
                        screen.render(String.format("抱歉没找到 %s", s));
                    } else {
                        cm.context().renderCurrentWord();
                    }
                }
            }
        }
    }
}
