package com.wan.departmentservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wan.departmentservice.domain.po.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
