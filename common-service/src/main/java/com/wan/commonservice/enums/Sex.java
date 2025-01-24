package com.wan.commonservice.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum Sex {
    MALE(1, "男"),
    FEMALE(2, "女");

    private final int code;
    @EnumValue
    private final String name;

    Sex(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
