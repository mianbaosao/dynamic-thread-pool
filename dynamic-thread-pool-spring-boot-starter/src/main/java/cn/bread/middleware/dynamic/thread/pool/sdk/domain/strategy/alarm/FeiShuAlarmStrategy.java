package cn.bread.middleware.dynamic.thread.pool.sdk.domain.strategy.alarm;


import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.dto.AlarmMessageDTO;
import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.enums.AlarmStrategyEnum;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeiShuAlarmStrategy extends AbstractAlarmStrategy{
    @Override
    public String getStrategyName() {
        return AlarmStrategyEnum.FEI_SHU.getValue();
    }

    @Override
    public void send(AlarmMessageDTO message) throws ApiException {
        log.info("飞书告警未实现，仅用于测试策略模式, message: {}", message);
    }
}
