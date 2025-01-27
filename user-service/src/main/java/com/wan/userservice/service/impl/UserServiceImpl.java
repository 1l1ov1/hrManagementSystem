package com.wan.userservice.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
<<<<<<< Updated upstream
import com.wan.commonservice.domain.po.User;
=======
<<<<<<< Updated upstream
=======
import com.wan.commonservice.constant.RedisConstant;
import com.wan.commonservice.domain.po.User;
import com.wan.commonservice.enums.OnlineStatus;
>>>>>>> Stashed changes
>>>>>>> Stashed changes
import com.wan.commonservice.enums.ResponseStatusCodeEnum;
import com.wan.commonservice.exception.ArgumentNullException;
import com.wan.commonservice.exception.AuthenticationException;
import com.wan.commonservice.exception.ObjectNotFoundException;
import com.wan.commonservice.untils.RedisUtil;
import com.wan.userservice.domain.dto.UserDTO;

import com.wan.userservice.domain.vo.UserVo;
import com.wan.commonservice.enums.AccountStatus;
import com.wan.userservice.mapper.UserMapper;
import com.wan.userservice.service.UserService;
import com.wan.userservice.utils.AESUtil;
import com.wan.userservice.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    /**
     * 用户登录方法
     *
     * @param userDTO 用户登录信息，包含用户账号和密码
     * @return 登录成功的用户信息，包括用户详情和令牌
     * @throws AuthenticationException 当用户认证失败时抛出
     * @throws RuntimeException        当登录过程中发生其他异常时抛出
     */
    @Override
    public UserVo login(UserDTO userDTO, HttpServletRequest request) {
        try {
            // 验证用户登录信息的合法性
            checkUserDTO(userDTO);
            // 构建查询条件，根据用户账号查询用户信息
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getAccount, userDTO.getAccount());
            User user = userMapper.selectOne(lambdaQueryWrapper);
            user.setOnlineStatus(OnlineStatus.ONLINE);
            // 检查用户的账号状态和密码
            checkStatusAndPassword(userDTO, user);

            // 将用户信息转换为用户视图对象
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);

            // 使用联表查询一次性获取创建者信息
            LambdaQueryWrapper<User> creatorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            creatorLambdaQueryWrapper.eq(User::getId, user.getCreatorId());
            User creator = userMapper.selectOne(creatorLambdaQueryWrapper);
            userVo.setCreatorName(creator != null ? creator.getEmployeeName() : "不存在");

            // 为用户生成JWT令牌
            String token = jwtUtil.createToken(userVo.getId());
            userVo.setToken(token);
            // 设置token到redis
            RedisUtil.setRedisTemplate(redisTemplate);
            RedisUtil.set(RedisConstant.USER_TOKEN + userVo.getId(), token, 1L, TimeUnit.HOURS);
            return userVo;

        } catch (AuthenticationException e) {
            log.warn("Login failed: {}", e.getMessage(), e);
            throw e;
        } catch (IllegalArgumentException e) {
            log.warn("Invalid input: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during login: {}", e.getMessage(), e);
            throw new RuntimeException("登录失败，请重试");
        }
    }

    @Override
    public void logout(Long id) {
        User user = getById(id);
        if (user == null) {
            throw new ObjectNotFoundException("用户不存在");
        }
        // 如果说用户存在
        user.setOnlineStatus(OnlineStatus.OFFLINE);
        updateById(user);
    }

    @Override
    public String getCreatorName(Long creatorId) {
        User user = userMapper.selectById(creatorId);
        if (ObjectUtil.isNull(user)) {
            // 如果说对象为空，即对象不存在
            return null;
        }

        return user.getEmployeeName();
    }

    @Override
    public Map<Long, String> getCreatorNames(Collection<Long> creatorIds) {
        if (creatorIds == null || creatorIds.isEmpty()) {
            return Collections.emptyMap();
        }
        // 如果不为空，就去查询数据库
        List<User> users = userMapper.selectBatchIds(creatorIds);
        // 如果没有
        if (users == null) {
            log.warn("No users found for creator IDs: {}", creatorIds);
            users = Collections.emptyList();
        }
        return users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        User::getEmployeeName,
                        (existing, replacement) -> existing // 处理重复键，保留第一个值
                ));
    }

    @Override
    public Map<Long, List<User>> findUsersByDepartmentIds(Collection<Long> departmentIds) {
        if (CollectionUtil.isEmpty(departmentIds)) {
<<<<<<< Updated upstream
            return Collections.emptyMap();
=======
            throw new ArgumentNullException("部门ID列表不能为空");
>>>>>>> Stashed changes
        }
        // 如果说不空，就去查找对应的用户信息
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(User::getDepartmentId, departmentIds);
        // 全部的用户
        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        // 然后转换成map
        return users.stream()
                .collect(Collectors.groupingBy(User::getDepartmentId));
    }
<<<<<<< Updated upstream

=======
    
>>>>>>> Stashed changes
    private void checkStatusAndPassword(UserDTO userDTO, User user) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        // 如果用户不存在，抛出异常提示用户未找到
        if (ObjectUtil.isNull(user)) {
            log.warn("User with account [REDACTED] not found");
            throw new AuthenticationException(ResponseStatusCodeEnum.USER_NOT_FOUND, "账号错误，用户不存在");
        }

        // 如果用户账号被锁定，抛出异常提示账号被锁定
        if (user.getAccountStatus() == AccountStatus.LOCKED) {
            throw new AuthenticationException(ResponseStatusCodeEnum.ACCOUNT_LOCKED, "账号被锁定，请联系管理员");
        }

        // 验证用户密码是否正确，使用安全的字符串比较方法
        if (!StrUtil.equals(user.getPassword(), AESUtil.encryption(userDTO.getPassword()))) {
            throw new AuthenticationException(ResponseStatusCodeEnum.USER_NOT_FOUND, "密码错误");
        }
    }

    private void checkUserDTO(UserDTO userDTO) {
        if (StrUtil.isBlank(userDTO.getAccount())) {
            throw new ArgumentNullException("用户名不能为空");
        }

        if (StrUtil.isBlank(userDTO.getPassword())) {
            throw new ArgumentNullException("密码不能为空");
        }

        if (userDTO.getPassword().length() > 12 || userDTO.getPassword().length() < 6) {
            throw new ArgumentNullException("密码长度必须大于6位小于12位");
        }

    }


}
