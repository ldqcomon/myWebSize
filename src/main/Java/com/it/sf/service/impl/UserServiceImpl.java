package com.it.sf.service.impl;

import com.it.sf.common.ApplicationContextHelper;
import com.it.sf.dao.UserMapper;
import com.it.sf.model.UserVo;
import com.it.sf.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @Auther: ldq
 * @Date: 2020/8/17
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public void saveUser(UserVo userVo) {
        userMapper.saveUser(userVo);
    }

    @Override
    public void updateUser(UserVo userVo) {
        userMapper.updateUser(userVo);
    }

    @Override
    public UserVo getUser(UserVo userVo) {
       return userMapper.getUser(userVo);
    }

}
