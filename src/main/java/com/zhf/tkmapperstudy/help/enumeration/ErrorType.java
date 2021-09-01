package com.zhf.tkmapperstudy.help.enumeration;

import lombok.Getter;

@Getter
public enum ErrorType {

    SYSTEM_ERROR("-1", "系统异常"),
    SYSTEM_BUSY("500001", "系统繁忙,请稍候再试"),
    SYSTEM_NOT_LOGIN("500003", "未登录"),
    SYSTEM_AUTH_FAIL("500004", "验证失败"),
    SYSTEM_REPEAT("500005", "有重复数据"),
    ACCOUNT_NOT_EXIST("500006", "账号不存在"),
    LOGIN_ERROR("500007", "账号或密码不正确"),
    ACCOUNT_EXIST("500005", "账户名称已存在"),
    USERNAME_EXIST("500008", "用户名已存在"),
    OLD_PASSWORD_WRONG("500009", "原密码不正确"),
    DATA_NOT_EXIST("500010", "数据不存在"),
    ROLE_NAME_EXIST("500011", "角色名重复"),
    ARGUMENT_NOT_VALID("520000", "请求参数校验不通过"),
    UPLOAD_FILE_SIZE_LIMIT("520001", "上传文件大小超过限制"),
    SYSTEM_TOKEN_EMPTY("520002", "token为空"),
    PHONE_EXIST("520003", "该用户已存在"),
    GET_USER_INFO_FAIL("520009", "获取微信用户信息失败"),
    QPS_REQUEST_LIMIT_REACHED("520011", ""),
    PHONE_FORMAT_WRONG("520004", "手机号格式不正确");

    /**
     * 错误类型码
     */
    private final String code;
    /**
     * 错误类型描述信息
     */
    private final String message;

    ErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
