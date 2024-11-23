package cn.bread.middleware.dynamic.thread.pool.sdk.config;

import cn.bread.middleware.dynamic.thread.pool.sdk.config.properties.DynamicThreadPoolAlarmAutoProperties;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.service.IAlarmService;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.service.impl.AlarmServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "dynamic-thread-pool.alarm", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(DynamicThreadPoolAlarmAutoProperties.class)
@ComponentScan("cn.bread.middleware.dynamic.thread.pool.sdk.domain.strategy")
public class DynamicThreadPoolAlarmAutoConfig {

    @Bean
    public IAlarmService alarmService() {
        return new AlarmServiceImpl();
    }
}
