package com.wan.userservice.controller;


import cn.hutool.core.util.ObjectUtil;
import com.wan.commonservice.domain.po.User;
import com.wan.commonservice.domain.vo.Result;
import com.wan.commonservice.enums.ResponseStatusCodeEnum;
import com.wan.userservice.domain.dto.UserDTO;
import com.wan.userservice.domain.vo.UserVo;
import com.wan.userservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

<<<<<<< Updated upstream
import java.util.Collection;
=======
<<<<<<< Updated upstream
=======
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
>>>>>>> Stashed changes
>>>>>>> Stashed changes
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "用户相关接口")
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public Result userLogin(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        UserVo userVo = userService.login(userDTO, request);
        if (ObjectUtil.isNull(userVo)) {
            return Result.fail(ResponseStatusCodeEnum.RESULT_IS_NOT_EXIST, "用户不存在");
        }
        return Result.success(userVo);
    }

    @PostMapping("/logout/{id}")
    @ApiOperation(value = "注销")
    public Result userLogout(@PathVariable Long id) {
        userService.logout(id);
        return Result.success();
    }

    @PostMapping("/getCreatorName")
    @ApiOperation(value = "获取创建者名称 供其他微服务远程调用")
    public String getCreatorName(@RequestBody Long creatorId) {
        return userService.getCreatorName(creatorId);
    }

    @PostMapping("/getCreatorNames")
    @ApiOperation(value = "获取创建者名称 供其他微服务远程调用")
    public Map<Long, String> getCreatorNames(@RequestBody List<Long> creatorIds) {
        return userService.getCreatorNames(creatorIds);
    }

    @PostMapping("/getUsersByDepartmentId")
    @ApiOperation(value = "根据部门id查询用户")
    Map<Long, List<User>> findUsersByDepartmentId(@RequestBody Collection<Long> departmentIds) {
       return userService.findUsersByDepartmentIds(departmentIds);
    }
}
