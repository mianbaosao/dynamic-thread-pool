package cn.bread.middleware.dynamic.thread.pool.sdk.web.controller;


import cn.bread.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.bread.middleware.dynamic.thread.pool.sdk.model.dto.RefreshThreadPoolConfigDTO;
import cn.bread.middleware.dynamic.thread.pool.sdk.model.dto.UpdateThreadPoolConfigDTO;
import cn.bread.middleware.dynamic.thread.pool.sdk.utils.RedisUtils;
import cn.bread.middleware.dynamic.thread.pool.sdk.web.exception.BusinessException;
import cn.bread.middleware.dynamic.thread.pool.sdk.web.model.enums.ResponseEnum;
import cn.bread.middleware.dynamic.thread.pool.sdk.web.model.vo.ResponseVO;
import cn.bread.middleware.dynamic.thread.pool.sdk.web.utils.AuthUtils;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "${dynamic-thread-pool.web.context-path}/pool")
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost*", "http://127.0.0.1*"})
public class ThreadPoolController {

    @Resource
    private RTopic dynamicThreadPoolAdjustRedisTopic;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RTopic dynamicThreadPoolRefreshRedisTopic;

    @GetMapping("/list")
    public ResponseVO<List<ThreadPoolConfigEntity>> getThreadPoolConfigList(HttpServletRequest request) {
        if (!AuthUtils.hashAuth(request)) {
            throw new BusinessException(ResponseEnum.NO_AUTH);
        }

        return ResponseVO.success(RedisUtils.getPoolConfigRList(redissonClient));
    }

    @GetMapping("/get")
    public ResponseVO<ThreadPoolConfigEntity> getThreadPoolUpdateConfig(
            HttpServletRequest request,
            String poolName) {
        if (!AuthUtils.hashAuth(request)) {
            throw new BusinessException(ResponseEnum.NO_AUTH);
        }

        ThreadPoolConfigEntity poolConfig = RedisUtils.getPoolConfigByPoolName(redissonClient, poolName);
        if (poolConfig == null) {
            throw new BusinessException(ResponseEnum.NOT_FOUNT, "未找到该线程池");
        }

        return ResponseVO.success(poolConfig);
    }

    @PostMapping("/update")
    public ResponseVO<Boolean > updateThreadPoolConfig(
            HttpServletRequest request,
            @RequestBody UpdateThreadPoolConfigDTO updateThreadPoolConfigDTO ) {
        if (!AuthUtils.hashAuth(request)) {
            throw new BusinessException(ResponseEnum.NO_AUTH);
        }

        dynamicThreadPoolAdjustRedisTopic.publish(updateThreadPoolConfigDTO);
        return ResponseVO.success(true, "成功发布线程池变更消息");
    }

    @GetMapping("/refresh")
    public ResponseVO<Void> refreshThreadPoolConfigList(HttpServletRequest request) {
        if (!AuthUtils.hashAuth(request)) {
            throw new BusinessException(ResponseEnum.NO_AUTH);
        }
        dynamicThreadPoolRefreshRedisTopic.publish(new RefreshThreadPoolConfigDTO());
        return ResponseVO.success(null);
    }
}
