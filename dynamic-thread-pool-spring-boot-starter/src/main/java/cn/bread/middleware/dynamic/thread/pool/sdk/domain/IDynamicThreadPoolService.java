package cn.bread.middleware.dynamic.thread.pool.sdk.domain;

import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @Description: 动态线程池服务
 * @Author:bread
 * @Date: 2024-06-03 15:39
 */
public interface IDynamicThreadPoolService {

    //查询所有线程池的集合
    List<ThreadPoolConfigEntity> queryThreadPoolList();

    //查询单个线程池by name
    ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName);

    //update
    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);

}
