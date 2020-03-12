package com.sky.wordmem.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/2/20
 * @description com.sky.wordmem
 */
@EqualsAndHashCode(callSuper = true)
@Component
@Data
class WordsContext extends BaseContext {
    @Override
    void close() {
        metaDao.preserveCursor(getMaxCursor());
    }
}
