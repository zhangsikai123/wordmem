package com.sky.wordmem.utils;

import java.util.List;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/27/20
 * @description com.sky.wordmem.utils
 */
public class SearchUtil {

    public static  <T> int searchObjectInList(List<T> list, T target){
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals(target)){
                return i;
            }
        }
        return -1;
    }
}
