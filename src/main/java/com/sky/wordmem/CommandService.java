package com.sky.wordmem;

import com.sky.wordmem.context.ContextManager;
import com.sky.wordmem.handlers.Command;
import com.sky.wordmem.handlers.Handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 3/2/20
 * @description com.sky.wordmem
 */
@Component
@Slf4j
public class CommandService implements ApplicationContextAware {

    private Map<String, Handler> handlerMapping = new HashMap<>();
    @Autowired
    ContextManager cm;
    private ApplicationContext applicationContext;


    private Handler getHandler(Request request) {
        if (!handlerMapping.containsKey(request.getCommand())) {
            throw Exceptions.unknownCommandException(request.getCommand());
        } else {
            return handlerMapping.get(request.getCommand());
        }
    }

    public void service(Request request) {
        Handler handler = getHandler(request);
        handler.handle(cm.context(), request);
    }

    public void init() {
        applicationContext.getBeansOfType(Handler.class).forEach((name, bean) -> {
                    Command command = applicationContext.findAnnotationOnBean(name, Command.class);
                    if(command != null) {
                        String[] handlerCommands = command.value();
                        for (String handlerCommand : handlerCommands) {
                            if(handlerMapping.containsKey(handlerCommand)){
                                throw Exceptions.onStartException(String.format("duplicate handler for %s", command));
                            }
                            handlerMapping.put(handlerCommand, bean);
                        }
                    }
                });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
