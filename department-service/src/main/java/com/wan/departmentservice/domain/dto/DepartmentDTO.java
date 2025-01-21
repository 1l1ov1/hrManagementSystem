package com.wan.departmentservice.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;


import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel(description = "创建部门实体")
public class DepartmentDTO {
    @ApiModelProperty(value = "部门ID", example = "1", required = false)
    private Long id;

    @ApiModelProperty(value = "部门名称", example = "研发部", required = true)
    private String departName;

    @ApiModelProperty(value = "部门描述", example = "负责产品研发", required = true)
    private String description;

    @ApiModelProperty(value = "父部门名称", required = false)
    private String parentDepartmentName;

    @ApiModelProperty(value = "是否启用", example = "true", required = true)
    private Boolean isEnabled;

    @ApiModelProperty(value = "是否删除", example = "false", required = true)
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建者ID",  required = true)
    private Long creatorId;
}
