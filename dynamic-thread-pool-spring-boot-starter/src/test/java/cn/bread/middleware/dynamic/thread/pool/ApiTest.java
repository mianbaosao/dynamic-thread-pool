package cn.bread.middleware.dynamic.thread.pool;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.redisson.api.RTopic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Description: 测试类
 * @Author:bread
 * @Date: 2024-05-26 20:06
 */
public class ApiTest {

    @Resource
    private RTopic dynamicThreadPoolRedisTopic;
}
