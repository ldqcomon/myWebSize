package com.it.sf.dao;

import com.it.sf.model.UserVo;

public interface UserMapper {
    void saveUser(UserVo userVo);
    void updateUser(UserVo userVo);
    UserVo getUser(UserVo userVo);
}