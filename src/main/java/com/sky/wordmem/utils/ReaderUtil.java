package com.sky.wordmem.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.utils
 */
@Slf4j
public class ReaderUtil {

    public static String readFile(String pathToFile) {
        try {
            ClassPathResource resource = new ClassPathResource(pathToFile);
            InputStream inputStream = resource.getInputStream();
            return new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e.toString());
        }
    }
}
