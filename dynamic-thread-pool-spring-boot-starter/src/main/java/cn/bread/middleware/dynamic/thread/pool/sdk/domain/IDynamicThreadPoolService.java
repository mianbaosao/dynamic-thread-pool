package cn.bread.middleware.dynamic.thread.pool.sdk.domain;

import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @Description: 动态线程池服务
 * @Author:bread
 * @Date: 2024-06-03 15:39
 */
public interface IDynamicThreadPoolService {

    List<ThreadPoolConfigEntity> queryThreadPoolList();


    ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName);


    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);

}
