package cn.bread.middleware.dynamic.thread.pool.sdk.utils;

import org.springframework.context.ApplicationContext;

public class ApplicationUtils {
    public static String getApplicationName(ApplicationContext applicationContext) {
        return applicationContext.getEnvironment().getProperty("spring.application.name");
    }
}
