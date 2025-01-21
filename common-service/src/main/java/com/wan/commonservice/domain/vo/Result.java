package com.wan.commonservice.domain.vo;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wan.commonservice.enums.ResponseStatusCodeEnum;
import lombok.Data;

@Data
public class Result {
    // private ResponseStatusCode status;

    private Integer code;

    private String msg;

    private Object data;

    public Result(ResponseStatusCodeEnum status, Object data) {

        if (ObjectUtils.isNotNull(status)) {
            this.code = status.getCode();
            this.msg = status.getDescription();
        }
        this.data = data;
    }

    private Result(Integer code, String msg) {
        this(code, msg, null);
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success() {
        return new Result(ResponseStatusCodeEnum.OK, null);
    }

    public static Result success(Object data) {
        return new Result(ResponseStatusCodeEnum.OK, data);
    }

    public static Result fail(ResponseStatusCodeEnum status) {
        try {
            return new Result(status.getCode(), status.getDescription());
        } catch (NullPointerException e) {
            return new Result(ResponseStatusCodeEnum.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public static Result fail(ResponseStatusCodeEnum status, String msg) {
        return new Result(status, msg);
    }

    public static Result fail(Integer code, String msg) {
        return new Result(code, msg);
    }
}
