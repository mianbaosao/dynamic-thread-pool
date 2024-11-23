package cn.bread.middleware.dynamic.thread.pool.sdk.domain.service;



import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.dto.AlarmMessageDTO;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * 告警服务类
 */
public interface IAlarmService {

    void send(AlarmMessageDTO message);

    void sendIfThreadPoolHasDanger(List<ThreadPoolConfigEntity> pools);
}
