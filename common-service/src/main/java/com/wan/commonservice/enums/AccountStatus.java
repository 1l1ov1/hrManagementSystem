package com.wan.userservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum AccountStatus {
    /**
     * 账号正常
     */
    NORMAL(1, "正常"),
    /**
     * 账号锁定
     */
    LOCKED(-1, "锁定");
    // 标识数据库中存储的值
    private final Integer code;

    @EnumValue
    private String desc;

    AccountStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    AccountStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
