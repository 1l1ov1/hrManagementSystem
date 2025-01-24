package com.wan.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wan.commonservice.domain.po.User;
import com.wan.userservice.domain.dto.UserDTO;

import com.wan.userservice.domain.vo.UserVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    /**
     * 登录
     * @return 返回userVo
     */
    UserVo login(UserDTO userDTO);

    /**
     * 根据创建者Id，获取创建者姓名
     * @param creatorId 创建者Id
     * @return 返回创建者姓名
     */
    String getCreatorName(Long creatorId);

    /**
     * 根据创建者Id集合，获取创建者姓名集合
     * @param creatorIds 创建者的Id集合
     * @return 返回Map Key为创建者Id，Value为创建者姓名
     */
    Map<Long, String> getCreatorNames(Collection<Long> creatorIds);

    /**
     * 查询部门下的所有用户
     * @param departmentIds 部门id集合
     * @return 返回Map Key为部门Id，Value为部门下的所有用户
     */
    Map<Long, List<User>> findUsersByDepartmentIds(Collection<Long> departmentIds);
}
