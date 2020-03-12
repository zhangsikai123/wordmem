package com.sky.wordmem.context;

import java.util.Date;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/9/20
 * @description com.sky.wordmem.context
 */
public class CountSession {

    private int startCursor;
    private Date startTime;

    public CountSession(int startCursor, Date startTime){
        this.startCursor = startCursor;
        this.startTime = startTime;
    }
}
