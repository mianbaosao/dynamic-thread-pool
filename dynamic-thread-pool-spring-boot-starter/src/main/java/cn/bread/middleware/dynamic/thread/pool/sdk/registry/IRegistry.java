package cn.bread.middleware.dynamic.thread.pool.sdk.registry;

import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @Description:  注册中心接口
 * @Author:bread
 * @Date: 2024-06-03 19:21
 */
public interface IRegistry {

    void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolEntities);

    /**
     * 上报配置参数
     * @param threadPoolConfigEntity
     */
    void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity);
}
