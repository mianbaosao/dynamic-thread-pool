package cn.bread.middleware.dynamic.thread.pool.sdk.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "dynamic-thread-pool.registry")
public class DynamicThreadPoolRegistryAutoProperties {
    //每二十秒
    private String reportCron = "0/20 * * * * ?";
}
