package com.wan.commonservice.enums;

public enum ResponseStatusCodeEnum {
    OK(200, "请求成功"),
    CREATED(201, "资源已创建"),
    ACCEPTED(202, "请求已接受，处理尚未完成"),
    NO_CONTENT(204, "请求成功但无内容返回"),
    BAD_REQUEST(400, "请求无效，请检查输入参数"),
    UNAUTHORIZED(401, "未授权，请登录或提供有效凭证"),
    FORBIDDEN(403, "禁止访问，您没有权限执行此操作"),
    NOT_FOUND(404, "资源未找到"),
    METHOD_NOT_ALLOWED(405, "请求方法不被允许"),
    NOT_ACCEPTABLE(406, "无法满足请求的格式"),
    REQUEST_TIMEOUT(408, "请求超时，请稍后再试"),
    CONFLICT(409, "请求冲突，资源已被修改"),
    GONE(410, "资源已被删除"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误，请联系管理员"),
    NOT_IMPLEMENTED(501, "服务未实现"),
    BAD_GATEWAY(502, "网关错误"),
    SERVICE_UNAVAILABLE(503, "服务暂时不可用，请稍后再试"),
    GATEWAY_TIMEOUT(504, "网关超时，请稍后再试"),

    ARGUMENT_NULL(601, "缺少必要参数"),
    RESULT_IS_NOT_EXIST(602, "查询结果不存在"),

    USER_NOT_FOUND(603, "用户名或密码错误"),
    ACCOUNT_LOCKED(604, "账号被锁定，请联系管理员"),
    OBJECT_IS_EXIST(605, "对象已存在"),
    DELETE_IS_FAIL(606, "删除失败"),

    OBJECT_NOT_FOUNT(607, "对象不存在"),
    UPDATE_IS_FAIL(608, "修改失败");
    private final int code;
    private final String description;

    ResponseStatusCodeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code + " " + description;
    }
}
