package com.sky.wordmem;

import lombok.Data;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/2/20
 * @description com.sky.wordmem
 */
@Data
public class Request {

    public static final String SEPARATOR = " ";

    String command;
    String arg;

    public Request(String command, String arg){
        this.command = command.toLowerCase();
        this.arg = arg.toLowerCase();
    }
}
