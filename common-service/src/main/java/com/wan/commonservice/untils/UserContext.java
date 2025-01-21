package com.wan.commonservice.untils;

public class UserContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置用户ID
     *
     * @param userId 用户id
     */
    public static void setUserId(Long userId) {
        threadLocal.set(userId);
    }

    /**
     * 得到用户id
     *
     * @return 用户Id
     */
    public static Long getUserId() {
        return threadLocal.get();
    }

    /**
     * 清除
     */
    public static void clear() {
        threadLocal.remove();
    }
}
