package com.wan.commonservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum OnboardingStatus {
    /**
     * 实习生
     */
    INTERNSHIP(1),
    /**
     * 正式员工
     */
    REGULAR(2),
    /**
     * 离职
     */
    LEAVE(3);

    @EnumValue
    private final Integer status;

    OnboardingStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
