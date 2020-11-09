package com.it.sf.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: ldq
 * @Date: 2020/8/30
 * @Description:登录拦截器
 * @Version: 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 登录页面不拦截
        String uri = request.getRequestURI();
        String username = (String) request.getSession().getAttribute("username");
        // 在此处 jsp页面拦截不了
        if (uri.contains("chat.html") && !StringUtils.isBlank(username)) {
            return true;
        }
        if (StringUtils.equals("/demo/",uri)
                || uri.contains("index/")
                || uri.contains("js/")
                || uri.contains("css/")
                || uri.contains("images/")
                || uri.contains("musics/")
                || uri.contains("jsp/z")
                || uri.contains("jsp/p")
        ) {
            return true;
        }
        if (StringUtils.isNotBlank(username)) {
                return true;
        }
        request.setAttribute("noLogin","您还没有登录");
        //request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        response.sendRedirect("/demo/jsp/login.jsp");

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
