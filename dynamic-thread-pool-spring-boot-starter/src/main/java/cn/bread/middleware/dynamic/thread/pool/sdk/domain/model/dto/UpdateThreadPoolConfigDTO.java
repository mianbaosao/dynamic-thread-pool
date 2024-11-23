package cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.dto;


import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateThreadPoolConfigDTO {
    private String applicationName;
    private String threadPoolName;
    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Integer queueCapacity;

    public static UpdateThreadPoolConfigDTO buildUpdateThreadPoolConfigDTO(
            ThreadPoolConfigEntity threadPoolConfigEntity ) {
        return new UpdateThreadPoolConfigDTO(
                threadPoolConfigEntity.getApplicationName(),
                threadPoolConfigEntity.getThreadPoolName(),
                threadPoolConfigEntity.getCorePoolSize(),
                threadPoolConfigEntity.getMaximumPoolSize(),
                threadPoolConfigEntity.getQueueSize() + threadPoolConfigEntity.getRemainingCapacity()
        );
    }
}
