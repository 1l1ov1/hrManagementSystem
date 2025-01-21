package com.wan.userservice.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户登录信息")
public class UserDTO {
    @ApiModelProperty(value = "用户登录的账号")
    private String account;
    @ApiModelProperty(value = "用户登录的密码")
    private String password;
}
