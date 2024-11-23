package cn.bread.middleware.dynamic.thread.pool.sdk.trigger.job;


import cn.bread.middleware.dynamic.thread.pool.sdk.config.properties.DynamicThreadPoolRegistryAutoProperties;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.dto.UpdateThreadPoolConfigDTO;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.registry.IRegistry;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.service.IDynamicThreadPoolService;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;


/**
 * @Description: 线程池数据上报任务
 * @Author:bread
 * @Date: 2024-06-03 19:51
 */

@Slf4j
@AllArgsConstructor
@EnableConfigurationProperties(DynamicThreadPoolRegistryAutoProperties.class)
public class ThreadPoolDataReportJob {

    private IDynamicThreadPoolService dynamicThreadPoolService;

    private IRegistry registry;

    @Scheduled(cron = "${dynamic-thread-pool.registry.report-cron}")
    public void reportThreadPoolData() {
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        registry.reportThreadPool(threadPoolConfigEntities);

        log.info("动态线程池, 上报线程池信息: {}", threadPoolConfigEntities);

        // 遍历每个线程池信息, 上报配置信息
        threadPoolConfigEntities.forEach(threadPoolConfigEntity -> {
            UpdateThreadPoolConfigDTO updateThreadPoolConfigDTO = UpdateThreadPoolConfigDTO
                    .buildUpdateThreadPoolConfigDTO(threadPoolConfigEntity);

            registry.reportUpdateThreadPoolConfigParameter(updateThreadPoolConfigDTO);

            log.info("动态线程池, 上报线程池配置信息: {}", updateThreadPoolConfigDTO);
        });
    }
}
