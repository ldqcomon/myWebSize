package com.it.sf.controller;

import com.it.sf.common.JsonData;
import com.it.sf.model.UserVo;
import com.it.sf.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: ldq
 * @Date: 2020/8/17
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Data
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/addUser")
    @ResponseBody
    public JsonData addUser(@RequestBody UserVo userVo) {
        log.info("hello");
        userService.saveUser(userVo);
        JsonData success = JsonData.success(userVo,"success");
        return success;
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public JsonData updateUser(@RequestParam("username") String username) {
        log.info(username);
        return JsonData.success();
    }

    @RequestMapping("/getUser")
    @ResponseBody
    public JsonData getUser(@RequestBody UserVo userVo) {
        log.info("com on!");
        UserVo user = userService.getUser(userVo);
        return user==null?JsonData.fail("username or password is wrong"):JsonData.success(user);
    }

}
