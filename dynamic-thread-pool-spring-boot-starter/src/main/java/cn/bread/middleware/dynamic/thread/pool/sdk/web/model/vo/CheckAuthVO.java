package cn.bread.middleware.dynamic.thread.pool.sdk.web.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CheckAuthVO implements Serializable {
    Boolean isLogin;
}
