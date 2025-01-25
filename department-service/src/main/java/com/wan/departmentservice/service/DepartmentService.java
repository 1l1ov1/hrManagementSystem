package com.wan.departmentservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wan.commonservice.domain.dto.PageDTO;
import com.wan.departmentservice.domain.dto.DepartmentDTO;
import com.wan.departmentservice.domain.po.Department;
import com.wan.departmentservice.domain.vo.DepartmentVo;

import java.util.List;

public interface DepartmentService {
    /**
     * 根据id查询部门信息
     * @param id 部门id
     * @return 返回部门Vo实体
     */
    DepartmentVo getDepartmentById(Long id);

    /**
     * 保存部门信息
     * @param departmentDTO 部门
     */
    void saveDepartment(DepartmentDTO departmentDTO);

    /**
     * 获取所有部门的列表（包括逻辑删除和未启用的）
     * @return 返回一个DepartmentVo（包含了父级部门的名称）
     */
    List<DepartmentVo> getDepartments();

    /**
     * 获取所有部门信息，只包含启用和未删除的部门信息
     * @return 返回一个DepartmentVo（包含了父级部门的名称）
     */
    List<DepartmentVo> getDepartmentsWithEnabled();

    /**
     * 分页查询所有部门
     * @param pageDTO 分页对象DTO
     * @return 返回分页结果
     */
    Page<DepartmentVo> pageQuery(PageDTO pageDTO);

    /**
     * 删除部门
     * @param ids 部门的集合
     * @return 返回是否删除成功
     */
    Boolean deleteDepartments(List<Long> ids);

    /**
     *
     * @param departmentDTO
     * @return
     */
    boolean updateDepartment(DepartmentDTO departmentDTO);
}
