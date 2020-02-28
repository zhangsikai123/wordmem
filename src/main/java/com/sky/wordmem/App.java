package com.sky.wordmem;

import com.sky.wordmem.adaptor.dictAdaptor.DictAdaptor;
import com.sky.wordmem.adaptor.dictAdaptor.vo.SearchResponse;
import com.sky.wordmem.dao.NoteDao;
import com.sky.wordmem.dao.WordDao;
import com.sky.wordmem.entity.NoteEntity;
import com.sky.wordmem.entity.WordEntity;
import com.sky.wordmem.module.NotebookModule;
import com.sky.wordmem.module.ScreenModule;
import com.sky.wordmem.script.ReloadWords;
import com.sky.wordmem.utils.CommonUtil;
import com.sky.wordmem.utils.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> profiles = Arrays.asList(SpringContextUtil.getEnv().getActiveProfiles());
        if (profiles.contains("test")) {
            return;
        }
        Scanner scanner = new Scanner(System.in);
        int cursor = 0;
        LinkedList<Integer> history = new LinkedList<>();
        WordDao wordDao = SpringContextUtil.getBean("wordDao");
        ScreenModule screen = SpringContextUtil.getBean("screenModule");
        NotebookModule notebook = SpringContextUtil.getBean("notebookModule");
        DictAdaptor dict = SpringContextUtil.getBean("marriamWebster");
        List<WordEntity> words = wordDao.selectAllOrdered();

        if (words.isEmpty()) {
            screen.render("词库是空的，重新加载。。。请稍等哈");
            ReloadWords reloadWords = SpringContextUtil.getBean("reloadWords");
            reloadWords.run();
            words = wordDao.selectAllOrdered();
        }

        List<String> values = words.stream().map(WordEntity::getValue)
                .collect(Collectors.toList());

        String forceCommand = "";

        while (true) {
            String s = StringUtils.isEmpty(forceCommand) ? scanner.nextLine() : forceCommand;
            forceCommand = "";

            if ("".equals(s)) {
                //NEXT
                history.add(cursor);
                cursor++;
                cursor = cursor % words.size();
                screen.render(words.get(cursor));
            } else if ("exit".equals(s)) {
                screen.render("谢谢使用，下次再见，白衣白衣~");
                break;
            } else if ("b".equals(s)) {
                if (history.size() > 0) {
                    cursor = history.pollLast();
                    screen.render(words.get(cursor));
                } else {
                    screen.render("最后一个了");
                }
            } else if (s.startsWith("n ")) {
                String note = s.substring(2);
                WordEntity word = words.get(cursor);
                notebook.writeNoteUnderWord(word, note);
                screen.render(word);
            } else if (s.startsWith("dn ")) {
                WordEntity word = words.get(cursor);
                try {
                    int index = Integer.parseInt(s.substring(3));
                    notebook.deleteNoteUnterWord(word, index);
                    screen.render(word);
                }catch (NumberFormatException e){
                    screen.render("dn only takes number as parameter, please re-input");
                }
            } else if ("progress".equals(s)) {
                screen.render(String.format("%s/%s", cursor, values.size()));
            } else if ("today".equals(s)) {
                List<WordEntity> todayProgress = notebook.todayProgress();
                screen.renderTodayProgress(todayProgress);
                if (!CollectionUtils.isEmpty(todayProgress)) {
                    forceCommand = todayProgress.get(todayProgress.size() - 1).getValue();
                }
            } else if ("speed".equals(s)) {
                screen.render(String.format("current speed: %s个单词/分钟，继续加油！", notebook.countSpeed()));
            }else if ("no chinese".equals(s)){
                screen.setNoChinese(true);
                screen.render(words.get(cursor));
            }else if ("show chinese".equals(s)){
                screen.setNoChinese(false);
                screen.render(words.get(cursor));
            }else if (s.startsWith("fw")){
                String query;
                if(s.length() > 3) {
                    query = s.substring(3);
                } else{
                    query = words.get(cursor).getValue();
                }
                SearchResponse res = dict.searchWord(query);
                dict.render(res);
            }
            else {
                history.add(cursor);
                cursor = searchObjectInList(values, s);
                if (cursor < 0) {
                    //没找到，回滚刚刚的操作
                    if (history.size() > 0) {
                        cursor = history.pollLast();
                    }
                    screen.render(String.format("抱歉没找到 %s", s));
                } else {
                    screen.render(words.get(cursor));
                }
            }
        }
    }
}
