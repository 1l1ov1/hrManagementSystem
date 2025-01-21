package com.wan.departmentservice.enums;

public enum DepartmentStatusEnum{
    /**
     * 禁用状态
     */
    DISABLED(0),

    /**
     * 启用状态
     */
    ENABLED(1),

    /**
     * 已删除状态
     */
    DELETED(0),

    /**
     * 未删除状态
     */
    NOT_DELETED(1);

    private final int value;

    DepartmentStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

