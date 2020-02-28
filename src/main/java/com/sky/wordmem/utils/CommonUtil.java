package com.sky.wordmem.utils;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.utils
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;
import org.sqlite.date.DateFormatUtils;

public class CommonUtil {

    public static String strMd5(String str){
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] hash = md.digest();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }
    public static String getStackTrace(StackTraceElement[] stackTraces) {
        StringBuilder msg = new StringBuilder();
        for (StackTraceElement e : stackTraces) {
            msg.append(e.toString()).append("\n");
        }
        return msg.toString();
    }

    public static <E> E ifNullThen(E e, E elseValue) {
        return e == null ? elseValue : e;
    }

    public static JSONObject mutableJSONObject(List<Object> args) {
        JSONObject obj = new JSONObject();
        obj.putAll(mutableMap(args));
        return obj;
    }

    public static <K, V> Map<K, V> mutableMap(List<Object> args) {
        if (args.size() % 2 != 0) {
            throw new RuntimeException("mutableMap takes list with size of even number");
        }
        HashMap<K, V> map = new HashMap<>();
        if (CollectionUtils.isEmpty(args)) {
            return map;
        }
        assert args.size() % 2 == 0;
        for (int i = 0; i < args.size(); i += 2) {
            K key = (K) args.get(i);
            V value = (V) args.get(i + 1);
            map.put(key, value);
        }
        return map;
    }

    public static String datetimeToStr(Date date) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Long datetimeToLong(Date date) {
        if (date == null) {
            return null;
        }
        return date.getTime() / 1000;
    }

    /**
     * 将多参数变为set
     *
     * @param <T>
     * @param args
     * @return
     */
    public static <T> HashSet<T> asSet(T... args) {
        HashSet<T> ret = new HashSet<T>();
        for (T item : args) {
            ret.add(item);
        }
        return ret;
    }

    /**
     * 将多参数变为list
     *
     * @param <T>
     * @param args
     * @return
     */
    public static <T> List<T> asList(T... args) {
        List<T> ret = new ArrayList<>();
        for (T item : args) {
            ret.add(item);
        }
        return ret;
    }

    public static <T, R> List<R> mapData(Iterable<T> iterableData, Function<T, R> mathOperation) {
        List<R> ret = new ArrayList<R>();
        iterableData.forEach(item -> {
            R transData = mathOperation.apply(item);
            ret.add(transData);
        });
        return ret;
    }

    /**
     * 将列表迭代，返回一个新list，元素为函数执行结果
     *
     * @param <T>
     * @param <R>
     * @param listData
     * @param mathOperation 参数两个：index，value
     * @return
     */
    public static <T, R> List<R> mapList(List<T> listData, BiFunction<Integer, T, R> mathOperation) {
        List<R> ret = new ArrayList<R>();
        Integer i = 0;
        for (T item : listData) {
            R transData = mathOperation.apply(i, item);
            ret.add(transData);
            i++;
        }
        return ret;
    }

    /**
     * 对map进行迭代，并返回一个list
     *
     * @param <T>           map的值
     * @param <R>           函数返回值
     * @param mapping       待迭代HashMap
     * @param mathOperation 操作函数，接受两个参数：hashmap的键和值
     * @return
     */
    public static <T, R> List<R> mapHashMap(Map<String, T> mapping, BiFunction<String, T, R> mathOperation) {
        List<R> ret = new ArrayList<R>();
        if (mapping == null) {
            return ret;
        }
        for (Entry<String, T> entry : mapping.entrySet()) {
            R result = mathOperation.apply(entry.getKey(), entry.getValue());
            ret.add(result);
        }
        return ret;
    }

    /**
     * 给不完整的url补上protocol：例：//img.xiaohongshu.com/a3gz6v
     *
     * @param imageLink
     * @param protocol
     * @return 例：http://img.xiaohongshu.com/a3gz6v
     */
    public static String fillImageLinkProtocol(String imageLink, String protocol) {
        if (imageLink != null) {
            if (!imageLink.startsWith("http")) {
                imageLink = String.format("%s:%s", protocol, imageLink);
            }
        }

        return imageLink;
    }

    public static String fillImageLinkProtocol(String imageLink) {
        return fillImageLinkProtocol(imageLink, "https");
    }

    /**
     * 将列表排序，返回排序后的结果（注：开心说比较慢，推荐数据大量（成千上万级别）时不要使用）
     *
     * @param <T>
     * @param rawList 待排序列表，这个列表本身顺序不会改变
     * @param key     如rawList是Object的list，传入想按照哪个attribute排序的key。例：传"weight"意味着按cls.weight的值排序
     * @param reverse true：从小到大；false：从大到小
     * @return
     */
    public static <T> List<T> sorted(List<T> rawList, String key, Boolean reverse) {
        // 未测试
        List<T> sortedList = new ArrayList<>(rawList);
        Collections.sort(sortedList, new Comparator<T>() {
            @Override
            public int compare(T a, T b) {
                try {
                    Float weightA = 0f;
                    Float weightB = 0f;
                    if (key == null) {
                        weightA = Float.parseFloat(a.toString());
                        weightB = Float.parseFloat(b.toString());
                    } else {
                        weightA = Float.parseFloat(a.getClass().getDeclaredField(key).get(a).toString());
                        weightB = Float.parseFloat(b.getClass().getDeclaredField(key).get(b).toString());
                    }
                    if (weightA == weightB) {
                        return 0;
                    } else if (reverse) {
                        return weightA < weightB ? 1 : -1;
                    } else {
                        return weightA > weightB ? 1 : -1;
                    }

                } catch (Exception e) {
                    return 0;
                }
            }
        });
        return sortedList;
    }

    /**
     * 数组中是否存在任意不是null的值
     *
     * @param <T>
     * @param data
     * @return
     */
    public static <T> Boolean any(Collection<T> data) {
        if (CollectionUtils.isEmpty(data)) {
            return false;
        }
        for (T d : data) {
            if (d != null) {
                return true;
            }
        }
        return false;
    }

    public static <T> T find(Iterable<T> iterableData, Function<T, Boolean> mathOperation) {
        for (T data : iterableData) {
            if (mathOperation.apply(data)) {
                return data;
            }
        }
        return null;
    }

    public static boolean equalObject(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        }
        if (o1 != null) {
            return o1.equals(o2);
        }
        return false;
    }
}
