package com.wan.commonservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum OnlineStatus {
    ONLINE(1),
    OFFLINE(0);
    @EnumValue
    private final Integer status;

    OnlineStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
