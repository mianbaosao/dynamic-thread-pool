package cn.bread.middleware.dynamic.thread.pool.sdk.config;



import cn.bread.middleware.dynamic.thread.pool.sdk.config.properties.DynamicThreadPoolWebAutoProperties;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.web.config.FrontendConfig;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.web.controller.AuthController;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.web.controller.SettingsController;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.web.controller.ThreadPoolController;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.web.exception.DynamicThreadPoolWebGlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty(prefix = "dynamic-thread-pool.web", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(DynamicThreadPoolWebAutoProperties.class)
public class DynamicThreadPoolWebAutoConfig {

//    @Bean
    public FrontendConfig dynamicThreadPoolStaticConfig(DynamicThreadPoolWebAutoProperties dynamicThreadPoolWebAutoProperties
    ) {
        return new FrontendConfig(dynamicThreadPoolWebAutoProperties);
    }

//    @Bean
    public AuthController dynamicThreadPoolAuthController() {
        return new AuthController();
    }

//    @Bean
    public ThreadPoolController dynamicThreadPoolThreadPoolController() {
        return new ThreadPoolController();
    }

//    @Bean
    public SettingsController dynamicThreadPoolSettingsController() {
        return new SettingsController();
    }

    @Bean
    public DynamicThreadPoolWebGlobalExceptionHandler dynamicThreadPoolWebGlobalExceptionHandler() {
        return new DynamicThreadPoolWebGlobalExceptionHandler();
    }
}
