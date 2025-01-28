package com.wan.departmentservice.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wan.apiservice.client.UserClient;
import com.wan.commonservice.domain.dto.PageDTO;
import com.wan.commonservice.domain.po.User;
import com.wan.commonservice.exception.ArgumentNullException;
import com.wan.commonservice.exception.ObjectExistedException;
import com.wan.commonservice.exception.ObjectNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    private final DepartmentMapper departmentMapper;

    private final UserClient userClient;

    /**
     * 根据部门ID获取部门视图对象
     * <p>
     * 当通过部门ID查询部门信息时，此方法用于将查询到的部门对象转换为部门视图对象
     * 如果部门对象不存在，则抛出异常，否则，将部门对象封装到列表中，并转换为部门视图对象列表，
     * 最后返回列表中的第一个元素
     *
     * @param id 部门ID，用于唯一标识一个部门
     * @return DepartmentVo 返回转换后的部门视图对象，如果找不到指定ID的部门，则抛出异常
     */
    @Override
    public DepartmentVo getDepartmentById(Long id) {
        // 根据ID获取部门对象
        Department department = getById(id);
        // 检查部门对象是否为空
        if (ObjectUtil.isNull(department)) {
            // 如果部门对象为空，则返回null
            throw new ArgumentNullException("部门不存在");
        }
        // 如果说部门存在
        return convertToDepartmentVo(department);
    }

    @Override
    public void saveDepartment(DepartmentDTO departmentDTO) {
        validateDepartmentDTO(departmentDTO);

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
        lambdaQueryWrapper.eq(Department::getId, departmentDTO.getParentId());
        Department parentDepartment = departmentMapper.selectOne(lambdaQueryWrapper);
        if (ObjectUtil.isNotNull(parentDepartment)) {
            // 如果说存在
            department.setParentId(parentDepartment.getId());
        } else {
            throw new ArgumentNullException("父级部门不存在");
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
        return convertDepartmentListToVoList(departmentList);

    }


    @Override
    public List<DepartmentVo> getDepartmentsWithEnabled() {
        // 要获取到所有启用的部门
        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Department::getIsEnabled, DepartmentStatusEnum.ENABLED.getValue());
        // 得到启用的部门
        List<Department> departmentListWithEnable = list(lambdaQueryWrapper);
        return convertDepartmentListToVoList(departmentListWithEnable);
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
        List<DepartmentVo> departmentVoList = convertDepartmentListToVoList(records);
        // 然后拷贝
        Page<DepartmentVo> departmentVoPage = new Page<>();
        BeanUtils.copyProperties(page, departmentVoPage);
        departmentVoPage.setRecords(departmentVoList);
        departmentVoPage.setTotal(page.getTotal());
        return departmentVoPage;
    }

    /**
     * 删除部门
     *
     * @param ids 部门的集合
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDepartments(List<Long> ids) {
        // 去重处理
        Set<Long> uniqueIds = new HashSet<>(ids);
        if (uniqueIds.isEmpty()) {
            throw new ArgumentNullException("传入的部门ID为空");
        }

        // 查找对应的部门
        List<Department> departments = departmentMapper.selectBatchIds(new ArrayList<>(uniqueIds));
        if (departments.size() != uniqueIds.size()) {
            Set<Long> existingIds = departments.stream().map(Department::getId).collect(Collectors.toSet());
            Set<Long> missingIds = new HashSet<>(uniqueIds);
            missingIds.removeAll(existingIds);
            throw new ObjectNotFoundException("以下部门ID不存在：" + missingIds);
        }

        // 构建子部门和员工查询条件
        LambdaQueryWrapper<Department> childDeptQuery = new LambdaQueryWrapper<>();
        childDeptQuery.in(Department::getParentId, uniqueIds);
        List<Department> childDepartments = departmentMapper.selectList(childDeptQuery);

        Map<Long, List<User>> users = userClient.findUsersByDepartmentId(new ArrayList<>(uniqueIds));

        // 合并子部门和员工检查逻辑
        checkDependencies(departments, childDepartments, users);

        return removeBatchByIds(new ArrayList<>(uniqueIds));
    }


    /**
     * @param departmentDTO 部门信息
     * @return 返回更新结果
     */
    @Override
    @Transactional
    public boolean updateDepartment(DepartmentDTO departmentDTO) {
        // 校验部门信息
        validateDepartmentDTO(departmentDTO);

        Long id = departmentDTO.getId();
        if (id == null) {
            throw new ArgumentNullException("部门ID不能为空");
        }

        Department department = departmentMapper.selectById(id);
        if (department == null) {
            throw new ObjectNotFoundException("部门不存在");
        }
        // 要判断是否修改的部门名字已存在
        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<Department>();
        lambdaQueryWrapper.eq(Department::getDepartName, departmentDTO.getDepartName())
                // 不是同一个部门
                .ne(Department::getId, id);
        // 得到数据库中的名称相同的部门
        Department departmentInDB = departmentMapper.selectOne(lambdaQueryWrapper);

        // 如果说二者不是同一个部门
        if (departmentInDB != null) {
            // 那么就抛错
            throw new ObjectExistedException("部门名称已经存在");
        }
        // 如果说是同一个部门或者说部门名称不存在
        // 检查启用变禁用或逻辑删除
        if (isStatusChange(departmentDTO, department)) {
            Map<Long, List<User>> users = userClient.findUsersByDepartmentId(Collections.singletonList(id));
            if (users != null && !users.isEmpty() && users.get(id).isEmpty()) {
                throw new ObjectExistedException("该部门存在员工，无法禁用");
            }

            // 还要看是否有子部门
            LambdaQueryWrapper<Department> departmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            // 找子部门
            departmentLambdaQueryWrapper.eq(Department::getParentId, id);
            // 找子部门是否是启用 或者 找子部门是否是没有被逻辑删除
            departmentLambdaQueryWrapper.and(wrapper -> wrapper
                    .eq(Department::getIsEnabled, DepartmentStatusEnum.ENABLED.getValue())
                    .or()
                    .eq(Department::getIsDeleted, DepartmentStatusEnum.NOT_DELETED.getValue()));

            if (departmentMapper.selectCount(departmentLambdaQueryWrapper) > 0) {
                throw new ObjectExistedException("该部门存在正在启用的子部门，无法禁用或删除");
            }
        }

        // 正常更新
        BeanUtils.copyProperties(departmentDTO, department);
        return updateById(department);
    }

    private boolean isStatusChange(DepartmentDTO departmentDTO, Department department) {
        Boolean newEnabled = departmentDTO.getIsEnabled();
        Boolean originalEnabled = department.getIsEnabled();
        Boolean newDeleted = departmentDTO.getIsDeleted();
        Boolean originalDeleted = department.getIsDeleted();

        // 确保布尔值不为 null
        newEnabled = newEnabled == null ? originalEnabled : newEnabled;
        newDeleted = newDeleted == null ? originalDeleted : newDeleted;

        return (!newEnabled && originalEnabled) || (newDeleted && !originalDeleted);
    }

    /**
     * 检查部门是否可以被删除
     * 该方法主要目的是确保待删除的部门不存在子部门或包含员工若存在，则抛出异常阻止删除操作
     *
     * @param departments      所有部门的列表，用于后续查找部门名称
     * @param childDepartments 子部门的列表，用于确定哪些部门是其他部门的父部门
     * @param users            部门ID映射到用户列表的映射，用于确定哪些部门包含员工
     * @throws ObjectExistedException 如果部门存在子部门或员工，则抛出此异常
     */
    private void checkDependencies(List<Department> departments, List<Department> childDepartments, Map<Long, List<User>> users) {
        // 收集所有有子部门的父部门ID
        Set<Long> parentIdsWithChildren = childDepartments.stream()
                .map(Department::getParentId)
                .collect(Collectors.toSet());

        // 收集所有有用户的父部门ID
        Set<Long> parentIdsWithUsers = users.keySet();

        // 创建一个包含所有被阻塞的父部门ID的集合，包括有子部门和有用户的部门
        Set<Long> blockedParentIds = new HashSet<>(parentIdsWithChildren);
        blockedParentIds.addAll(parentIdsWithUsers);

        // 如果有被阻塞的父部门ID
        if (!blockedParentIds.isEmpty()) {
            // 根据部门ID收集所有被阻塞的部门名称
            List<String> blockedNames = departments.stream()
                    .filter(department -> blockedParentIds.contains(department.getId()))
                    .map(Department::getDepartName)
                    .collect(Collectors.toList());

            // 根据被阻塞的部门是否拥有子部门，抛出不同的异常信息
            if (!parentIdsWithChildren.isEmpty()) {
                throw new ObjectExistedException("以下部门存在子部门，不能删除部门：" + blockedNames);
            } else {
                throw new ObjectExistedException("以下部门存在员工，不能删除部门：" + blockedNames);
            }
        }
    }


    /**
     * 获取到部门vo列表（会根据传进来的departmentList，来查询对应的父级部门
     *
     * @param departmentList 部门列表
     * @return departmentVo列表
     */
    @NotNull
    // 批量转换 Department 列表为 DepartmentVo 列表
    public List<DepartmentVo> convertDepartmentListToVoList(List<Department> departmentList) {
        if (CollectionUtil.isEmpty(departmentList)) {
            return Collections.emptyList();
        }

        // 批量查询创建者姓名
        Map<Long, String> creatorNames = getCreatorNames(departmentList);
        // 批量查询父级部门信息
        Map<Long, Department> parentDepartmentMap = getParentDepartmentMap(departmentList);

        return departmentList.stream()
                .map(department -> convertToDepartmentVo(department, creatorNames, parentDepartmentMap))
                .collect(Collectors.toList());
    }

    // 单个转换 Department 为 DepartmentVo
    public DepartmentVo convertToDepartmentVo(Department department) {
        Map<Long, String> creatorNames = getCreatorNames(Collections.singletonList(department));
        Map<Long, Department> parentDepartmentMap = getParentDepartmentMap(Collections.singletonList(department));
        return convertToDepartmentVo(department, creatorNames, parentDepartmentMap);
    }

    // 转换单个 Department 为 DepartmentVo，使用已查询的创建者姓名和父级部门信息
    private DepartmentVo convertToDepartmentVo(Department department, Map<Long, String> creatorNames, Map<Long, Department> parentDepartmentMap) {
        DepartmentVo departmentVo = new DepartmentVo();
        BeanUtils.copyProperties(department, departmentVo);

        // 设置父级部门名称
        Long parentId = department.getParentId();
        if (parentId != null) {
            Department parentDepartment = parentDepartmentMap.get(parentId);
            if (parentDepartment != null) {
                departmentVo.setParentDepartName(parentDepartment.getDepartName());
            }
        }

        // 设置创建者姓名
        String creatorName = creatorNames.get(department.getCreatorId());
        if (StrUtil.isNotBlank(creatorName)) {
            departmentVo.setCreatorName(creatorName);
        }

        return departmentVo;
    }

    // 批量查询创建者姓名
    private Map<Long, String> getCreatorNames(List<Department> departmentList) {
        Set<Long> creatorIds = departmentList.stream()
                .map(Department::getCreatorId)
                .collect(Collectors.toSet());
        return userClient.getCreatorNames(creatorIds);
    }

    // 批量查询父级部门信息
    private Map<Long, Department> getParentDepartmentMap(List<Department> departmentList) {
        Set<Long> parentIds = departmentList.stream()
                .map(Department::getParentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (parentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        // 这里假设 list 方法用于查询部门列表
        LambdaQueryWrapper<Department> parentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parentLambdaQueryWrapper.in(Department::getId, parentIds);
        List<Department> parentDepartmentList = list(parentLambdaQueryWrapper);

        return parentDepartmentList.stream()
                .collect(Collectors.toMap(Department::getId, department -> department));
    }

    private void validateDepartmentDTO(DepartmentDTO departmentDTO) {
        if (ObjectUtil.isNull(departmentDTO)) {
            throw new ArgumentNullException("传入的部门信息为空");
        }

        if (StrUtil.isBlank(departmentDTO.getDepartName())) {
            throw new ArgumentNullException("部门名称不能为空");
        }

        if (StrUtil.isBlank(departmentDTO.getDescription())) {
            throw new ArgumentNullException("部门描述不能为空");
        }
    }

}
