package com.it.sf.controller;

import com.it.sf.common.HttpClientUtils;
import com.it.sf.common.JsonData;
import com.it.sf.common.VerifyCodeUtils;
import com.it.sf.model.UserVo;
import com.it.sf.service.RedisUtilService;
import com.it.sf.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

/**
 * @Auther: ldq
 * @Date: 2020/8/17
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Data
@RestController
@RequestMapping("/index")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtilService redisUtilService;
    private int count = 0;
    static{
        System.setProperty("java.awt.headless","true");
    }

    // 登录
    @RequestMapping("/login")
    public JsonData login(@RequestBody UserVo userVo, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        String username = userVo.getUsername();
        String password = userVo.getPassword();
        String verifyCode = userVo.getVerifyCode();
        if (StringUtils.isBlank(username)) {
            return JsonData.fail("the username is empty");
        }
        if (StringUtils.isBlank(password)) {
            return JsonData.fail("the password is empty");
        }
        if (StringUtils.isBlank(verifyCode)) {
            return JsonData.fail("the verifyCode is empty");
        } else {
            if (this.checkMobileNumber(username)) {
                userVo.setMobile(username);
                userVo.setUsername("");
            }
            UserVo user = userService.getUser(userVo);
            if (null == user) {
                return JsonData.fail("the username is not exist");
            }
            if (!StringUtils.equals(user.getPassword(), password)) {
                return JsonData.fail("the password is error");
            }
            if (null == request.getSession().getAttribute("verifyCode")) {
                return JsonData.fail("The verification code has expired!");
            }
            if (!StringUtils.equalsIgnoreCase(verifyCode, request.getSession().getAttribute("verifyCode").toString())) {
                return JsonData.fail("The verification code is error!");
            } else {
                session.setAttribute("username", username);
                // 设置session的有效时间为30分钟,单位:秒,也可在配置文件:web.xml中配置
                session.setMaxInactiveInterval(30 * 60);
                log.info("com in the service1.............");
                //request.getRequestDispatcher("/WEB-INF/views/chat.html").forward(request, response);
                return new JsonData(true);
            }
        }

    }

    // 注册
    @RequestMapping("/updatePassword")
    public JsonData updatePassword(@RequestBody UserVo userVo,HttpSession session) {
        String password = userVo.getPassword();
        String surePassword = userVo.getSurePassword();
        String mobile = userVo.getMobile();
        String verifyCode = userVo.getVerifyCode();
        String mobileCode = userVo.getMobileCode();
        if (StringUtils.isBlank(password)) {
            return JsonData.fail("the password is empty");
        }
        if (StringUtils.isBlank(surePassword)) {
            return JsonData.fail("the surePassword is empty");
        }
        if (!StringUtils.equals(password, surePassword)) {
            return JsonData.fail("the surePassword is different from password");
        }
        if (StringUtils.isBlank(mobile)) {
            JsonData.fail("the mobile is empty");
        }
        if (null == userService.getUser(userVo)) {
            JsonData.fail("the mobile user is not exist!");
        }
        if (StringUtils.isBlank(verifyCode)) {
            return JsonData.fail("the userCode is empty");
        }
        if (null == session.getAttribute("verifyCode")) {
            return JsonData.fail("the verifyCode is expired");
        }
        if (!StringUtils.equalsIgnoreCase(verifyCode, session.getAttribute("verifyCode").toString())) {
            return JsonData.fail("the verifyCode is error");
        }
        if (StringUtils.isBlank(mobileCode)) {
            return JsonData.fail("the mobileCode is empty");
        }
        if (StringUtils.isNotBlank(mobileCode)) {
            if (null == redisUtilService.getByCluster("mobileCode")) {
                return JsonData.fail("the mobileCode is expired");
            } else if (!StringUtils.equals(mobileCode, redisUtilService.getByCluster("mobileCode"))) {
                return JsonData.fail("the mobileCode is error");
            }
        }
        if (StringUtils.isNoneBlank(password, surePassword, mobile, verifyCode, mobileCode)) {
            userService.updateUser(userVo);
            return JsonData.success();
        }
        return null;
    }


    // 登出
    @RequestMapping("/logout")
    public void logout(HttpSession session,HttpServletResponse response) throws IOException {
        //清除session
        session.invalidate();
        //重定向到登录页面的跳转方法
        response.sendRedirect("/demo/jsp/login.jsp");
    }

    // 注册
    @RequestMapping("/register")
    public JsonData register(@RequestBody UserVo userVo, HttpSession session, HttpServletRequest request) throws ServletException, IOException {
        String username = userVo.getUsername();
        String password = userVo.getPassword();
        String surePassword = userVo.getSurePassword();
        String mobile = userVo.getMobile();
        String verifyCode = userVo.getVerifyCode();
        String mobileCode = userVo.getMobileCode();
        if (StringUtils.isBlank(username)) {
            return JsonData.fail("the username is empty");
        }
        if (StringUtils.isNoneBlank(username)) {
            if (null != userService.getUser(userVo)) {
                return JsonData.fail("the username have already exist");
            }
        }
        if (StringUtils.isBlank(password)) {
            return JsonData.fail("the password is empty");
        }
        if (StringUtils.isBlank(surePassword)) {
            return JsonData.fail("the surePassword is empty");
        }
        if (!StringUtils.equals(password, surePassword)) {
            return JsonData.fail("the surePassword is different from password");
        }
        if (StringUtils.isBlank(mobile)) {
            JsonData.fail("the mobile is empty");
        }
        if (StringUtils.isBlank(verifyCode)) {
            return JsonData.fail("the userCode is empty");

        }
        if (null == session.getAttribute("verifyCode")) {
            return JsonData.fail("the verifyCode is expired");
        }
        if (!StringUtils.equalsIgnoreCase(verifyCode, session.getAttribute("verifyCode").toString())) {
            return JsonData.fail("the verifyCode is error");
        }
        if (StringUtils.isBlank(mobileCode)) {
            return JsonData.fail("the mobileCode is empty");
        }
        if (StringUtils.isNotBlank(mobileCode)) {
            if (null == redisUtilService.getByCluster("mobileCode")) {
                return JsonData.fail("the mobileCode is expired");
            } else if (!StringUtils.equals(mobileCode, redisUtilService.getByCluster("mobileCode"))) {
                return JsonData.fail("the mobileCode is error");
            }
        }
        if (StringUtils.isNoneBlank(username, password, surePassword, mobile, verifyCode, mobileCode)) {
            userService.saveUser(userVo);
            return JsonData.success();
        }
        return null;
    }

    // 获取验证码
    @RequestMapping("/getVerifyCode")
    public void getVerificationCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            int width = 165;
            int height = 43;
            BufferedImage verifyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            String randomText = VerifyCodeUtils.drawRandomText(width, height, verifyImg);
            HttpSession session = request.getSession();
            session.setAttribute("verifyCode", randomText);
            response.setContentType("image/png");//必须设置响应内容类型为图片，否则前台不识别
            OutputStream os = response.getOutputStream(); //获取文件输出流
            ImageIO.write(verifyImg, "png", os);//输出图片流
            os.flush();
            os.close();//关闭流
            this.removeVerifyCode(session, "verifyCode", 300 * 1000);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    // 对比验证码
    @RequestMapping("/compareVerifyCode")
    public JsonData compareVerifyCode(@RequestBody UserVo userVo, HttpSession session) {
        String mobile = userVo.getMobile();
        String verifyCode = userVo.getVerifyCode();
        if (StringUtils.isAnyBlank(mobile, verifyCode)) {
            return JsonData.fail("手机或者验证码为空");
        }
        if (!this.checkMobileNumber(mobile)) {
            return JsonData.fail("手机号格式不对");
        }
        if (null == session.getAttribute("verifyCode")) {
            return JsonData.fail("验证码已经无效,请重新获取");
        } else {
            if (!StringUtils.equalsIgnoreCase(verifyCode, session.getAttribute("verifyCode").toString())) {
                return JsonData.fail("验证码错误");
            }
        }
        return JsonData.success();
    }

    // 生成短信验证码,并且远程掉接口发送到相应的手机上
    @RequestMapping("/getMobileCode")
    public JsonData getMobileCode(@RequestBody UserVo userVo, HttpSession session) {
        String mobile = userVo.getMobile();
        if (!this.checkMobileNumber(mobile)) {
            log.info("手机号格式不对:{}" + mobile);
            return JsonData.fail("手机号格式不对");
        }
        String mobileCode = RandomStringUtils.randomNumeric(6);

        if (count%3==0 && redisUtilService.keyExist(mobile)) {
            return JsonData.fail("同一个手机号在5分钟内不可以发送超过3个短信验证码");
        }
        // this.mobileCodeTimeout(session, mobile, mobileCode, 3 * 60*1000);
        HttpClientUtils client = HttpClientUtils.getInstance();

        //UTF发送
        // int result = client.sendMsgUtf8("hear", "d41d8cd98f00b204e980", "您本次操作验证码:" + mobileCode + "," + "有效时间15分钟", mobile);
        if (1 > 0) {
            log.info("发送短信验证码成功:{}",mobileCode);
            String result = this.mobileCodeTimeoutByRedis("mobileCode", mobileCode, 300, false);
            log.info("the key:mobileCode set int to redis:{}",result);
            String result2 = this.mobileCodeTimeoutByRedis(mobile, mobile, 300, true);
            count++;
            log.info("the key:"+mobile+" set int to redis:{}",result2);
            return JsonData.success();
        } else {
            log.info("发送短信验证码失败:{}", client.getErrorMsg(1));
            return JsonData.fail("短信发送失败");
        }
    }

    // 在session中设置验证码的有效时间
    private void removeVerifyCode(HttpSession session, String attributeName, long timeout) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                session.removeAttribute(attributeName);
                timer.cancel();
            }
        }, timeout);
    }

    // 检验手机号
    private boolean checkMobileNumber(String number) {
        return Pattern.matches("^(1[3-9])\\d{9}$", number);
    }

    // 通过session方式进行短信验证码的超时设置
    private JsonData mobileCodeTimeout(HttpSession session, String mobile, String mobileCode, long timeout) {
        // 加入一个小时内不能发超过5次短信验证码
        if (count > 2) {
            return JsonData.fail("发送短信操作太频繁,3分钟后才可以获取短信验证码");
        }
        session.setAttribute("mobileCode", mobileCode);
        this.removeVerifyCode(session, "mobileCode", timeout);
        session.setAttribute(mobile, count++);
        log.info("count:{}", count);
        return null;
    }

    // 通过redis缓存来设置超时
    private String mobileCodeTimeoutByRedis(String key, String value, int timeout, boolean isNx) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return isNx == true ? redisUtilService.setNxByCluster(key, value, timeout) :
                redisUtilService.setByCluster(key, value, timeout);
    }

}
