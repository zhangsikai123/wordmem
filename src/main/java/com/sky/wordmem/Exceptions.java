package com.sky.wordmem;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/2/20
 * @description com.sky.wordmem
 */
public class Exceptions{

    public static RuntimeException unknownCommandException(String arg){
        return new RuntimeException(String.format("unknown command for %s", arg));
    }

    public static RuntimeException onStartException(String arg){
        return new RuntimeException(String.format("error upon starting: %s", arg));
    }

    public static RuntimeException systemException(String arg){
        return new RuntimeException(String.format("unexpected error during runtime: %s", arg));
    }

    public static RuntimeException luceneException(String arg){
        return new RuntimeException(String.format("lucene error: %s", arg));
    }

    public static class TimerException extends RuntimeException{}
}
