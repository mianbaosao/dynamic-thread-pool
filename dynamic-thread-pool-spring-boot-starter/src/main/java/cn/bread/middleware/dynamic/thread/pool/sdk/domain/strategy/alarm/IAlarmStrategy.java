package cn.bread.middleware.dynamic.thread.pool.sdk.domain.strategy.alarm;


import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.dto.AlarmMessageDTO;
import com.taobao.api.ApiException;

public interface IAlarmStrategy {
    void send(AlarmMessageDTO message) throws ApiException;
}
