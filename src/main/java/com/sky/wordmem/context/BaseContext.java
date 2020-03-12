package com.sky.wordmem.context;

import com.sky.wordmem.Exceptions;
import com.sky.wordmem.Request;
import com.sky.wordmem.config.Lucene;
import com.sky.wordmem.dao.MetaDao;
import com.sky.wordmem.dao.WordDao;
import com.sky.wordmem.entity.WordEntity;
import com.sky.wordmem.module.ScreenModule;
import com.sky.wordmem.script.ReloadWords;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sky.wordmem.Request.SEPARATOR;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/4/20
 * @description com.sky.wordmem.context
 */
@Data
@Slf4j
public abstract class BaseContext {

    protected List<WordEntity> words;
    protected List<String> values;
    protected int cursor;
    protected int maxCursor;
    protected Date startTime;
    protected Request forceCommand;
    protected LinkedList<Integer> history;
    protected Scanner scanner;
    protected boolean inited;

    private boolean timerOn;
    private Date timerStartTime;

    /**
     * 记录打开计时器以来一共向前浏览多少个单词
     **/
    private int timerCollector;


    @Autowired
    protected ReloadWords reloadWords;
    @Autowired
    protected WordDao wordDao;
    @Autowired
    protected MetaDao metaDao;
    @Autowired
    protected ScreenModule screen;

    public Boolean isInitialized() {
        return inited;
    }

    public void init() {
        cursor = 0;
        List<WordEntity> words = wordDao.selectAllOrdered();
        if (words.isEmpty()) {
            screen.render("词库是空的，重新加载。。。请稍等哈");
            reloadWords.run();
            words = wordDao.selectAllOrdered();
        }
        List<String> values = words.stream().map(WordEntity::getValue)
                .collect(Collectors.toList());
        this.words = words;
        this.values = values;
        this.history = new LinkedList<>();
        this.scanner = new Scanner(System.in);
        this.startTime = new Date();
        this.cursor = Integer.parseInt(metaDao.getLastCursor().get(0));
        this.inited = true;
        renderCurrentWord();
    }

    public void refresh() {
        init();
    }

    public void setForceCommand(String s) {
        this.forceCommand = new Request(s, "");
    }

    public void setForceCommand(Request request) {
        this.forceCommand = request;
    }

    public void addCursor() {
        touchCursor();
        cursor++;
        cursor = cursor % words.size();
        maxCursor = Math.max(cursor, maxCursor);
        if (timerOn) {
            timerCollector++;
        }
    }

    public void deCursor() {
        touchCursor();
        cursor--;
        if (cursor < 0) {
            cursor = 0;
        }
    }

    private void touchCursor() {
        history.add(cursor);
    }

    public Request nextCommand() {
        Request command;
        if (forceCommand != null) {
            command = forceCommand;
            this.forceCommand = null;
            return command;
        }
        String l = scanner.nextLine().trim();
        if (l.contains(SEPARATOR)) {
            int sep = l.indexOf(SEPARATOR);
            command = new Request(l.substring(0, sep), l.substring(sep + 1));
        } else {
            command = new Request(l, "");
        }
        return command;
    }

    public WordEntity currentWord() {
        if (words.size() == 0) {
            log.info("没有单词了 ~");
            return null;
        }
        if (cursor >= words.size() || cursor < 0) {
            log.error(String.format("cursor index error: %s", cursor));
            return null;
        }
        return words.get(cursor);
    }

    public void renderCurrentWord() {
        screen.render(currentWord());
    }

    abstract void close();

    public void openOrCloseTimer() {
        if (timerOn) {
            timerOn = false;
            timerCollector = 0;
        } else {
            timerOn = true;
            timerStartTime = new Date();
        }
    }

    public int countSpeed() {
        //默认 个/分钟
        checkIsOn();
        return Math.toIntExact(wordsSinceTimerOn() / minutesSinceTimerOn());
    }

    public long minutesSinceTimerOn() {
        Date now = new Date();
        long pastMinutes = Math.max(1, TimeUnit.MILLISECONDS.toMinutes(
                now.getTime() - getTimerStartTime().getTime()));
        return pastMinutes;
    }

    public int wordsSinceTimerOn() {
        checkIsOn();
        return timerCollector;
    }

    private void checkIsOn() {
        if (!timerOn) {
            throw new Exceptions.TimerException();
        }
    }
}
