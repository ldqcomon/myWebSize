package com.it.sf.controller;

import com.it.sf.common.JsonData;
import com.it.sf.model.UserVo;
import com.it.sf.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: ldq
 * @Date: 2020/8/17
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Data
@RestController
@RequestMapping("/page")
public class pageController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/toChatPage")
    public void toChatPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("hello");
        String username = request.getParameter("username");
        RequestContextHolder.currentRequestAttributes().setAttribute("username",username,0);
        request.setAttribute("username",username);
        request.getRequestDispatcher("/WEB-INF/views/chat.html").forward(request,response);

    }

    @RequestMapping(value = "/toLoginPage")
    public ModelAndView toLoginPage() {
        return new ModelAndView("login");

    }


}
