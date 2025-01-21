package com.wan.departmentservice.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "需要返回部门的信息")
public class DepartmentVo {
    @ApiModelProperty(value = "部门ID")
    private Long id;
    @ApiModelProperty(value = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    private String departName;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "父级部门ID")
    private Long parentId;

    @ApiModelProperty(value = "父级部门的名称")
    private String parentDepartName;

    @ApiModelProperty(value = "创建人ID")
    private Long creatorId;
    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "部门描述")
    private String description;

    @ApiModelProperty(value = "是否启用")
    private Boolean isEnabled = true;

    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted = false;
}
