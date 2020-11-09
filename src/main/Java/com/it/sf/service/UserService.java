package com.it.sf.service;

import com.it.sf.model.UserVo;

/**
 * @Auther: ldq
 * @Date: 2020/8/17
 * @Description:
 * @Version: 1.0
 */
public interface UserService {
    void saveUser(UserVo userVo);
    void updateUser(UserVo userVo);
    UserVo getUser(UserVo userVo);
}
