package com.wan.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wan.userservice.domain.po.User;
import com.wan.userservice.domain.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
