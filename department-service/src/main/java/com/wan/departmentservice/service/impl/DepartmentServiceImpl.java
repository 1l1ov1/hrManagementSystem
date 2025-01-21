package com.wan.departmentservice.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wan.apiservice.client.UserClient;
import com.wan.commonservice.domain.dto.PageDTO;
import com.wan.commonservice.exception.ArgumentNullException;
import com.wan.commonservice.exception.ObjectExistedException;
import com.wan.departmentservice.domain.dto.DepartmentDTO;
import com.wan.departmentservice.domain.po.Department;
import com.wan.departmentservice.domain.vo.DepartmentVo;
import com.wan.departmentservice.enums.DepartmentStatusEnum;
import com.wan.departmentservice.mapper.DepartmentMapper;
import com.wan.departmentservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    private final DepartmentMapper departmentMapper;

    private final UserClient userClient;

    @Override
    public DepartmentVo getDepartmentById(Long id) {
        return null;
    }

    @Override
    public void saveDepartment(DepartmentDTO departmentDTO) {
        if (ObjectUtil.isNull(departmentDTO)) {
            throw new ArgumentNullException("传入的部门信息为空");
        }

        if (StrUtil.isBlank(departmentDTO.getDepartName())) {
            throw new ArgumentNullException("部门名称不能为空");
        }

        if (StrUtil.isBlank(departmentDTO.getDescription())) {
            throw new ArgumentNullException("部门描述不能为空");
        }

        Department department = new Department();
        BeanUtils.copyProperties(departmentDTO, department);
        // 先判断部门名字是否已经存在
        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<Department>();
        lambdaQueryWrapper.eq(Department::getDepartName, departmentDTO.getDepartName());
        // 如果说已经存在了，就不允许创建
        if (ObjectUtil.isNotNull(departmentMapper.selectOne(lambdaQueryWrapper))) {
            throw new ObjectExistedException("部门名称已经存在");
        }
        // 如果说不存在
        // 先清空之前的查询条件
        lambdaQueryWrapper.clear();
        // 然后根据父级部门名字，查询对应的部门
        lambdaQueryWrapper.eq(Department::getDepartName, departmentDTO.getParentDepartmentName());
        Department parentDepartment = departmentMapper.selectOne(lambdaQueryWrapper);
        if (ObjectUtil.isNotNull(parentDepartment)) {
            // 如果说存在
            department.setParentId(parentDepartment.getId());
        }
        // 如果说不存在
        save(department);
    }


    @Override
    public List<DepartmentVo> getDepartments() {
        // 查询所有的部门信息
        List<Department> departmentList = list();
        // 如果说部门为空
        if (ObjectUtil.isEmpty(departmentList)) {
            return Collections.emptyList();
        }
        return getDepartmentVoList(departmentList);

    }


    @Override
    public List<DepartmentVo> getDepartmentsWithEnabled() {
        // 要获取到所有启用的部门
        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Department::getIsEnabled, DepartmentStatusEnum.ENABLED.getValue());
        // 得到启用的部门
        List<Department> departmentListWithEnable = list(lambdaQueryWrapper);
        return getDepartmentVoList(departmentListWithEnable);
    }

    /**
     * 根据分页请求参数进行部门信息的分页查询
     *
     * @param pageDTO 分页请求参数对象，包含页码、页面大小和排序方向等信息
     * @return 返回分页的部门信息，以DepartmentVo形式封装
     */
    @Override
    public Page<DepartmentVo> pageQuery(PageDTO pageDTO) {
        // 构建分页对象并根据创建时间进行默认排序
        Page<Department> page = page(pageDTO.toMapPageDefaultSortByCreateTime(pageDTO.getIsAsc()));
        // 得到结果，然后转成Vo
        List<Department> records = page.getRecords();
        List<DepartmentVo> departmentVoList = getDepartmentVoList(records);
        // 然后拷贝
        Page<DepartmentVo> departmentVoPage = new Page<>();
        BeanUtils.copyProperties(page, departmentVoPage);
        departmentVoPage.setRecords(departmentVoList);
        departmentVoPage.setTotal(page.getTotal());
        return departmentVoPage;
    }

    /**
     * 获取到部门vo列表（会根据传进来的departmentList，来查询对应的父级部门
     *
     * @param departmentList 部门列表
     * @return departmentVo列表
     */
    @NotNull
    private List<DepartmentVo> getDepartmentVoList(List<Department> departmentList) {
        // 查询所有的creatorId
        Map<Long, String> creatorNames = userClient.getCreatorNames(
                departmentList.stream()
                        .map(Department::getCreatorId)
                        .collect(Collectors.toSet()));
        // 先获取到所有的parentId
        Set<Long> parentIds = departmentList.stream().map(Department::getParentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        // 然后根据父级部门id查询父级部门
        LambdaQueryWrapper<Department> parentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果说parentIds为不空
        if (!parentIds.isEmpty()) {
            // 要id为parentId的部门
            parentLambdaQueryWrapper.in(Department::getId, parentIds);
        }

        // 查询
        List<Department> parentDepartmentList = list(parentLambdaQueryWrapper);
        // 创建Map来保存父级部门信息
        Map<Long, Department> parentDepartmentMap = parentDepartmentList.stream()
                .collect(Collectors.toMap(Department::getId, department -> department));

        // 遍历所有部门
        return departmentList.stream().map(department -> {
            // 创建vo
            DepartmentVo departmentVo = new DepartmentVo();
            // 拷贝
            BeanUtils.copyProperties(department, departmentVo);
            // 去查找父级部门
            Long parentId = department.getParentId();
            log.debug("parentId:{}", parentId);
            if (parentId != null) {
                // 如果存在
                Department parentDepartment = parentDepartmentMap.get(parentId);

                // 数据库中的数据不一致或不完整，导致某些部门的 parentId 指向了一个不存在的部门。（也许？）
                if (parentDepartment != null) {
                    departmentVo.setParentDepartName(parentDepartment.getDepartName());
                }

            }
            String creatorName = creatorNames.get(department.getCreatorId());
            log.debug("creatorName :{}", creatorName);
            // 如果说创建者姓名不空
            if (StrUtil.isNotBlank(creatorName)) {
                departmentVo.setCreatorName(creatorName);
            }
            return departmentVo;
        }).collect(Collectors.toList());
    }
}
