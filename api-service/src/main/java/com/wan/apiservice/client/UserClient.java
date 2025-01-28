package com.wan.apiservice.client;

import com.wan.commonservice.domain.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = "user-service")
public interface UserClient {
    /**
     * 根据创建者ID获取创建者名称
     *
     * @param creatorId 创建者ID
     * @return 创建者名称
     */
    @PostMapping("/users/getCreatorName")
    String getCreatorName(Long creatorId);

    /**
     * 根据创建者们的id，得到创建者们的名称
     *
     * @param creatorIds 创建者们的id
     * @return 创建者id和名称的Map集合
     */
    @PostMapping("/users/getCreatorNames")
    Map<Long, String> getCreatorNames(Collection<Long> creatorIds);

    /**
     * 根据部门id获取部门下的用户
     *
     * @param departmentIds 部门id集合
     * @return 返回部门下的用户集合
     */

    @PostMapping("/users/getUsersByDepartmentId")
    Map<Long, List<User>> findUsersByDepartmentId(@RequestBody Collection<Long> departmentIds);

}
