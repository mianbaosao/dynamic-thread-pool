package cn.bread.middleware.dynamic.thread.pool.sdk.web.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingsVO implements Serializable {

    private String apiContextPath;
    private String grafanaDashboardUrl;
}
