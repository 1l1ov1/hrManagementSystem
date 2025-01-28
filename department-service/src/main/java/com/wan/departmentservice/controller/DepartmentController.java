package com.wan.departmentservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wan.commonservice.domain.dto.PageDTO;
import com.wan.commonservice.domain.vo.Result;
import com.wan.commonservice.enums.ResponseStatusCodeEnum;
import com.wan.departmentservice.domain.dto.DepartmentDTO;
import com.wan.departmentservice.domain.po.Department;
import com.wan.departmentservice.domain.vo.DepartmentVo;
import com.wan.departmentservice.service.DepartmentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@Slf4j
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/create")
    @ApiOperation(value = "创建部门")
    public Result createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        departmentService.saveDepartment(departmentDTO);
        return Result.success();
    }

    @PostMapping("/pageQuery")
    @ApiOperation(value = "获取部门列表（分页）")
    public Result page(@RequestBody PageDTO pageDTO) {
        Page<DepartmentVo> pages = departmentService.pageQuery(pageDTO);
        return Result.success(pages);
    }

    @GetMapping("/getDepartments")
    @ApiOperation(value = "获取部门列表（所有部门）")
    public Result getDepartments() {
        List<DepartmentVo> departmentVoList = departmentService.getDepartments();
        return Result.success(departmentVoList);
    }

    @GetMapping("/getDepartmentsWithEnabled")
    @ApiOperation(value = "获取部门列表（仅启用的部门）")
    public Result getDepartmentsWithEnabled() {
        List<DepartmentVo> departmentVoList = departmentService.getDepartmentsWithEnabled();
        return Result.success(departmentVoList);
    }

    @GetMapping("/getDepartmentDetail/{id}")
    public Result getDepartmentDetail(@PathVariable Long id) {
        DepartmentVo departmentVo = departmentService.getDepartmentById(id);
        return Result.success(departmentVo);
    }

    @DeleteMapping("/delete")
    public Result deleteDepartments(@RequestParam List<Long> ids) {
        Boolean result = departmentService.deleteDepartments(ids);
        return result ? Result.success() : Result.fail(ResponseStatusCodeEnum.DELETE_IS_FAIL);
    }

    @PutMapping("/update")
    public Result updateDepartment(@RequestBody DepartmentDTO departmentDTO) {
        boolean res = departmentService.updateDepartment(departmentDTO);
        return res ? Result.success() : Result.fail(ResponseStatusCodeEnum.UPDATE_IS_FAIL);
    }
}
