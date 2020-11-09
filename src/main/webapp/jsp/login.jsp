<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>登录</title>
    <link type="text/css" rel="stylesheet" href="/demo/css/login.css" />
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/login.js"></script>
    <script type="text/javascript">
    </script>
</head>

<body>
<div class="main">
    <div class="main0">
        <div class="main_left">
            <img src="${pageContext.request.contextPath}/images/login-image-3.png" class="theimg"/>
            <img src="${pageContext.request.contextPath}/images/login-image-2.png" class="secimg"/>
            <img src="${pageContext.request.contextPath}/images/login-image-1.png" class="firimg"/>
        </div>
        <div class="main_right">
            <div class="main_r_up">
                <img src="${pageContext.request.contextPath}/images/user.png" />
                <div class="pp">登录</div>
            </div>
            <div class="sub"><p>还没有账号？<a href="/demo/jsp/zhuce.html"><span class="blue">立即注册</span></a></p></div>
            <div class="txt">
                <span style="letter-spacing:8px;">用户名:</span>
                <input name="username" id="username" type="text" required class="txtphone" placeholder="请输入注册手机号或用户名"/>
            </div>
            <div class="txt">
                <span style="letter-spacing:4px;">登录密码:</span>
                <input name="password" id="password" type="password" required class="txtphone" placeholder="请输入登录密码"/>
            </div>
            <div class="txt">
                <span style=" float:left;letter-spacing:8px;">验证码:</span>
                <input name="verifyCode" id="verifyCode" type="text" required class="txtyzm" placeholder="请输入验证码"/>
                <img src="/demo/index/getVerifyCode" id="verifyCodeIMG" onclick="changeCode()" class="yzmimg"/>
            </div>
            <div class="xieyi">
                <input name="" type="checkbox" value="" checked="checked"/>
                记住我 <a href="/demo/jsp/password.html"><span class="blue" style=" padding-left:130px; cursor:pointer">忘记密码?</span></a>
            </div>
            <div class="xiayibu">登录</div>
        </div>
    </div>
</div>

<div class="footer">
    <div class="footer0">
        <div class="footer_l">使用条款 | 隐私保护</div>
<%--        <div class="footer_r">© 2020 （深圳）黑洞信息科技服务有限公司    粤ICP备2020085941号</div>--%>
        <div style="width:400px;margin:0 auto; padding:20px 0;">
            <a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=44030902002311" style="display:inline-block;text-decoration:none;height:20px;line-height:20px;"><img src="/demo/images/gongan.png" style="float:left;"/>
                <p style="float:left;height:20px;line-height:20px;margin: 0px 0px 0px 5px; color:#939393;">粤公网安备 44030902002311号 粤ICP备2020085941号</p></a>
        </div>
    </div>
</div>
</body>
</html>
