package com.sky.wordmem.context;

import com.sky.wordmem.Exceptions;
import com.sky.wordmem.module.ScreenModule;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/4/20
 * @description com.sky.wordmem.context
 */
@Component
@Data
public class ContextManager implements ApplicationContextAware {

    ApplicationContext context;
    @Resource
    private BaseContext reviewContext;
    @Resource
    private BaseContext wordsContext;
    @Autowired
    ScreenModule screen;

    BaseContext currentContext;
    String currentContextTag;

    public static final String REVIEW = "review";
    public static final String DEFAULT = "default";

    public BaseContext context() {
        if (currentContext == null) {
            throw Exceptions.onStartException("context not set");
        }
        if (!currentContext.isInitialized()) {
            // 懒加载
            currentContext.init();
        }
        return currentContext;
    }

    public void init() {
        useContext(DEFAULT);
    }

    public void refresh(){
        currentContext.refresh();
    }

    public void useContext(String cxt) {
        currentContextTag = cxt;
        if (REVIEW.equals(cxt)) {
            screen.render("当前为: 复习模式，现在开始复习前三天学习过的单词，请集中注意力");
            currentContext = reviewContext;
        } else if (DEFAULT.equals(cxt)) {
            screen.render("当前为: 无聊的 普通模式，唉 无聊咋说来着？");
            currentContext = wordsContext;
        } else {
            Exceptions.onStartException(String.format("illegal cxt %s", cxt));
        }
    }

    public void close(){
        Map<String, BaseContext> all = context.getBeansOfType(BaseContext.class);
        all.forEach((name, cxt)-> cxt.close());
    }

    public void reset() {
        useContext(DEFAULT);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
