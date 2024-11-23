package cn.bread.middleware.dynamic.thread.pool.sdk.config;


import cn.bread.middleware.dynamic.thread.pool.sdk.config.properties.DynamicThreadPoolRegistryRedisAutoProperties;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.dto.RefreshThreadPoolConfigDTO;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.dto.UpdateThreadPoolConfigDTO;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.enums.RegistryEnum;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.registry.IRegistry;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.registry.redis.RedisRegistry;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.service.IAlarmService;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.service.IDynamicThreadPoolService;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.service.impl.DynamicThreadPoolServiceImpl;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.utils.ApplicationUtils;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.utils.RedisUtils;
import cn.bread.middleware.dynamic.thread.pool.sdk.trigger.job.ThreadPoolDataReportJob;
import cn.bread.middleware.dynamic.thread.pool.sdk.trigger.listener.ThreadPoolConfigAdjustListener;
import cn.bread.middleware.dynamic.thread.pool.sdk.trigger.listener.ThreadPoolConfigRefreshListener;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusProperties;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 自动配置入口
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicThreadPoolRegistryRedisAutoProperties.class)
@EnableScheduling
@ImportAutoConfiguration({DynamicThreadPoolWebAutoConfig.class, DynamicThreadPoolAlarmAutoConfig.class})
public class DynamicThreadPoolAutoConfig {

    @Bean
    public RedissonClient redissonClient(DynamicThreadPoolRegistryRedisAutoProperties properties) {
        Config config = new Config();

        config.setCodec(JsonJacksonCodec.INSTANCE);

        config.useSingleServer()
                .setAddress(String.format("redis://%s:%d", properties.getHost(), properties.getPort()))
                .setPassword(properties.getPassword())
                .setDatabase(properties.getDatabase())
                .setConnectionPoolSize(properties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(properties.getConnectionMinimumIdleSize())
                .setIdleConnectionTimeout(properties.getIdleConnectionTimeout())
                .setConnectTimeout(properties.getConnectTimeout())
                .setRetryAttempts(properties.getRetryAttempts())
                .setRetryInterval(properties.getRetryInterval())
                .setKeepAlive(properties.getKeepAlive());
        return Redisson.create(config);
    }

    @Bean
    public IRegistry redisRegistry(RedissonClient redissonClient, IAlarmService alarmService) {
        return new RedisRegistry(redissonClient, alarmService);
    }

    @Bean
    public ThreadPoolDataReportJob threadPoolDataReportJob(
            IDynamicThreadPoolService dynamicThreadPoolService,
            IRegistry redisRegistry) {
        return new ThreadPoolDataReportJob(dynamicThreadPoolService, redisRegistry);
    }


    @Bean
    public ThreadPoolConfigAdjustListener threadPoolConfigAdjustListener(
            IDynamicThreadPoolService dynamicThreadPoolService,
            IRegistry redisRegistry) {
        return new ThreadPoolConfigAdjustListener(dynamicThreadPoolService, redisRegistry);
    }


    @Bean
    public RTopic dynamicThreadPoolAdjustRedisTopic(
            ThreadPoolConfigAdjustListener threadPoolConfigAdjustListener,
            RedissonClient redissonClient) {
        RTopic topic = redissonClient.getTopic(RegistryEnum.DYNAMIC_THREAD_POOL_ADJUST_REDIS_TOPIC_KEY.getKey());
        topic.addListener(UpdateThreadPoolConfigDTO.class, threadPoolConfigAdjustListener);
        return topic;
    }

    @Bean
    public ThreadPoolConfigRefreshListener threadPoolConfigRefreshListener(
            IDynamicThreadPoolService dynamicThreadPoolService,
            IRegistry redisRegistry) {
        return new ThreadPoolConfigRefreshListener(dynamicThreadPoolService, redisRegistry);
    }

    @Bean
    public RTopic dynamicThreadPoolRefreshRedisTopic(
            ThreadPoolConfigRefreshListener threadPoolConfigRefreshListener,
            RedissonClient redissonClient) {
        RTopic topic = redissonClient.getTopic(RegistryEnum.DYNAMIC_THREAD_POOL_REFRESH_REDIS_TOPIC_KEY.getKey());
        topic.addListener(RefreshThreadPoolConfigDTO.class, threadPoolConfigRefreshListener);
        return topic;
    }

    @Bean
    public DynamicThreadPoolServiceImpl dynamicThreadPoolService(
            ApplicationContext applicationContext,
            Map<String, ThreadPoolExecutor> threadPoolExecutorMap,
            RedissonClient redissonClient,
            IAlarmService alarmService
    ) {
        String applicationName = ApplicationUtils.getApplicationName(applicationContext);
        if (StringUtils.isBlank(applicationName)) {
            log.warn("动态线程池启动提示。SpringBoot 应用未配置应用名(spring.application.name)");
        }

        // 创建Bean
        DynamicThreadPoolServiceImpl dynamicThreadPoolService = new DynamicThreadPoolServiceImpl(
                applicationName,
                threadPoolExecutorMap,
                alarmService
        );

        // 获取缓存的配置信息，配置线程池
        threadPoolExecutorMap.forEach((poolName, executor) -> {
            UpdateThreadPoolConfigDTO updateThreadPoolConfigDTO = RedisUtils.getUpdateThreadPoolConfigDTO(
                    redissonClient,
                    applicationName,
                    poolName
            );
            if (updateThreadPoolConfigDTO == null) {
                return;
            }


            dynamicThreadPoolService.updateThreadPoolConfig(
                    updateThreadPoolConfigDTO
            );

        });

        return dynamicThreadPoolService;
    }

    @Bean
    public PrometheusConfigRunner prometheusConfigRunner(
            ApplicationContext applicationContext,
            WebEndpointProperties webEndpointProperties,
            PrometheusProperties prometheusProperties
    ) {
        webEndpointProperties.getExposure().setInclude(
                new HashSet<>(Arrays.asList(
                        "health",
                        "prometheus"
                ))
        );

        prometheusProperties.setEnabled(true);

        return new PrometheusConfigRunner(
                applicationContext
        );
    }

    @Bean
    public MeterFilter customMeterFilter() {
        return new MeterFilter() {
            @Override
            public MeterFilterReply accept(Meter.Id id) {
                if (id.getName().contains("thread_pool")) {
                    return MeterFilterReply.ACCEPT;
                }
                return MeterFilterReply.DENY;
            }
        };
    }
}
