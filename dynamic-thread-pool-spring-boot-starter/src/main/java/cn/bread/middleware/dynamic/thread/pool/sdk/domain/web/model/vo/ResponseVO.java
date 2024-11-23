package cn.bread.middleware.dynamic.thread.pool.sdk.domain.web.model.vo;


import cn.bread.middleware.dynamic.thread.pool.sdk.domain.web.model.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO <T> implements Serializable {

    private Integer status;
    private String message;
    private String description;
    private T data;

    public static <T> ResponseVO<T> success(T data) {
        return ResponseVO.success(data, "");
    }

    public static <T> ResponseVO<T> success(T data, String description) {
        return new ResponseVO<>(
                ResponseEnum.SUCCESS.getStatus(),
                ResponseEnum.SUCCESS.getMessage(),
                description,
                data
        );
    }

    public static <T> ResponseVO<T> failed(ResponseEnum responseEnum) {
        return ResponseVO.failed(responseEnum, "");
    }

    public static <T> ResponseVO<T> failed(ResponseEnum responseEnum, String description) {
        return new ResponseVO<>(
                responseEnum.getStatus(),
                responseEnum.getMessage(),
                description,
                null
        );
    }
}
