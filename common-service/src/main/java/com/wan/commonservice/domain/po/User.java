package com.wan.commonservice.domain.po;

<<<<<<< Updated upstream
import com.wan.commonservice.enums.AccountStatus;
import com.wan.commonservice.enums.OnboardingStatus;
import com.wan.commonservice.enums.OnlineStatus;
import com.wan.commonservice.enums.Sex;

=======

import com.wan.userservice.enums.AccountStatus;
import com.wan.userservice.enums.OnboardingStatus;
import com.wan.userservice.enums.OnlineStatus;
import com.wan.userservice.enums.Sex;
>>>>>>> Stashed changes
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@ApiModel(description = "用户信息")
public class User {

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "工号")
    @NotBlank(message = "工号不能为空")
    private Long employeeId;

    @ApiModelProperty(value = "员工姓名")
    @NotBlank(message = "员工姓名不能为空")
    private String employeeName;

    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String account;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "身份证")
    @Pattern(regexp = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)", message = "身份证格式不正确")
    private String idCard;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    @ApiModelProperty(value = "部门ID")
    private Long departmentId;
    @ApiModelProperty(value = "账号状态")
    private AccountStatus accountStatus;
    @ApiModelProperty(value = "在线状态")
    private OnlineStatus onlineStatus;
    @ApiModelProperty(value = "性别")
    private Sex sex;

    @ApiModelProperty(value = "年龄")
    @Min(value = 18, message = "年龄不能为负数")
    @Max(value = 70, message = "年龄不能超过70岁")
    private Integer age;

    @ApiModelProperty(value = "员工入职状态")
    private OnboardingStatus onboardingStatus;

    @ApiModelProperty(value = "出生日期")
    private LocalDate birthday;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者Id")
    @NotBlank(message = "创建者Id不能为空")
    private Long creatorId;
<<<<<<< Updated upstream
}

=======
}
>>>>>>> Stashed changes
