// 文档加载完成事件
$(function () {
    var height = $(document).height();
    $('.main').css('height', height);
    // 表单校验:登录
    // var loginForm = document.forms["loginForm"];
    // var user = loginForm.elements['username'];
    // user.onchange=function(){};
    // ajax 提交表单数据
    $('.xiayibu').click(function () {
        let username = $('#username').val();
        let password = $('#password').val();
        let verifyCode = $('#verifyCode').val();
        let data = {"username": username, "password": password, "verifyCode": verifyCode};
        $.ajax({
            type: "post",
            dataType: "json",//服务器返回的数据类型
            data: JSON.stringify(data),
            contentType: "application/json;charset=UTF-8",// 表示传入的数据内容为json格式的
            url: "/demo/index/login",
            cache: false,
            async: false,
            success: function (result) {
                if (result.ret) {
                    //登录成功,跳到聊天室页面(encodeURI:编码)
                    window.location.href = '/demo/jsp/chat.html?username='+username;
                } else {
                    alert("登录失败,"+result.msg);
                }
            },
            error: function () {
                alert("sorry,the system is error");
            }

        });
    });

})

function checkPram(username, password, type) {
    // 请求后台接口,校验用户名是合法
    // var data = {username,password};
    $.ajax({
        type: "post",
        dataType: "json",
        data: JSON.stringify(data),
        contentType: "application/json;charset=UTF-8",
        url: "/demo/user/getUser",
        success: function (result) {
            if (!result.ret && type === 0) alert("用户名不正确");
            if (!result.ret && type === 1) alert("密码不正确");
        },
        error: function () {
            alert("sorry,the system is error");
        }

    });


}

// form 表单数据转换为json字符串
function formToJsonString(id) {
    var arr = $("#" + id).serializeArray();//form表单序列化
    var jsonStr = "";
    jsonStr += '{';
    for (var i = 0; i < arr.length; i++) {
        jsonStr += '"' + arr[i].name + '":"' + arr[i].value + '",';
    }
    jsonStr = jsonStr.substring(0, (jsonStr.length - 1));
    jsonStr += '}';
    var jsons = JSON.parse(jsonStr); //将拿到的键值对转换位json对象:{name: "仍然", email: "仍然", password: "rr"}
    return JSON.stringify(jsons); //返回json字符串:{"name":"仍然","email":"仍然","password":"rr"}
}


/*点击刷新验证码*/
function changeCode() {
    var src = "/demo/index/getVerifyCode?" + new Date().getTime(); //加时间戳，防止浏览器利用缓存
    $('#verifyCodeIMG').attr("src", src); //jQuery写法

}



