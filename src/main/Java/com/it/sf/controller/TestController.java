package com.it.sf.controller;

import com.it.sf.common.JsonData;
import com.it.sf.model.UserVo;
import com.it.sf.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Auther: ldq
 * @Date: 2020/8/17
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Data
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private UserService userService;
    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello() {
        log.info("hello");
        UserVo userVo = UserVo.builder().build();
        userVo.setId(1);
        userVo.setUsername("韩信");
        userVo.setUserCode("Dk454fDgfrg894a");
        userVo.setStatus(1);
        userService.saveUser(userVo);
        JsonData success = JsonData.success(userVo,"success");
        return success;
    }

    @RequestMapping("/hello2")
    @ResponseBody
    public JsonData hello2(@RequestParam("username") String username) {
        log.info(username);
        return JsonData.success();
    }

    // 设置首页测试--ok
    @RequestMapping("/gg")
    public ModelAndView index() {
        log.info("hello");
       // return new ModelAndView("admin");
        return new ModelAndView("chat_old");
    }
}
