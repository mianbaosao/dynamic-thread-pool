package cn.bread.middleware.dynamic.thread.pool.sdk.web.controller;


import cn.bread.middleware.dynamic.thread.pool.sdk.config.properties.DynamicThreadPoolWebAutoProperties;
import cn.bread.middleware.dynamic.thread.pool.sdk.web.model.vo.ResponseVO;
import cn.bread.middleware.dynamic.thread.pool.sdk.web.model.vo.SettingsVO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost*", "http://127.0.0.1*"})
public class SettingsController {

    @Resource
    private DynamicThreadPoolWebAutoProperties webAutoProperties;

    @GetMapping("/settingsLZQGJPUFNS")
    public ResponseVO<SettingsVO> settings() {
        return ResponseVO.success(new SettingsVO(
                webAutoProperties.getContextPath(),
                webAutoProperties.getGrafanaDashboardUrl()
        ));
    }
}
