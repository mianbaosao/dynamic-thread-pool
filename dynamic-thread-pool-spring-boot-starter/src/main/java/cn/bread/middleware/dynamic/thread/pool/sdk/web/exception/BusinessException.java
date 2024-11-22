package cn.bread.middleware.dynamic.thread.pool.sdk.web.exception;


import cn.bread.middleware.dynamic.thread.pool.sdk.web.model.enums.ResponseEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ResponseEnum responseEnum;
    private final String description;


    public BusinessException(ResponseEnum responseEnum) {
        this(responseEnum, "");
    }

    public BusinessException(ResponseEnum responseEnum, String description) {
        super(responseEnum.getMessage());
        this.responseEnum = responseEnum;
        this.description = description;
    }
}
