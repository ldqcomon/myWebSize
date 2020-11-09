window.onload = function () {
    // 屏幕的尺寸
    var screenW = document.documentElement.clientWidth;
    var screenH = document.documentElement.clientHeight;
    //页面加载完成时,从登陆页面获取用户名,设置用户
        /*获取到Url里面的参数*/
    (function ($) {
        $.getUrlParam = function (name) {
            const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            const r = encodeURI(window.location.search).substr(1).match(reg);
            if (r != null) return decodeURI(unescape(r[2])); return null;
        }
    })(jQuery)
    let username = $.getUrlParam("username");
    $('#user').val(username);

    // 2. 动态创建多个星星
    for (var i = 0; i < 150; i++) {
        var span = document.createElement('span');
        document.body.appendChild(span);

        //位置随机
        var x = parseInt(Math.random() * screenW);
        var y = parseInt(Math.random() * screenH);
        span.style.left = x + 'px';
        span.style.top = y + 'px';

        //大小随机
        var scale = Math.random() * 1.5;
        span.style.transform = 'scale(' + scale + ', ' + scale + ')';

        //频率随机
        var rate = Math.random() * 1.5;
        span.style.animationDelay = rate + 's';
    }

    //定时切换聊天背景图片(或者可以用animation 动画发生实现)
    var img= [
        "/demo/images/mn1.jpg", "/demo/images/mn2.jpg",
        "/demo/images/mn3.jpg", "/demo/images/mn4.jpg",
        "/demo/images/mn5.jpg", "/demo/images/mn6.jpg",
        "/demo/images/mn7.jpg", "/demo/images/mn8.jpg",
        "/demo/images/mn9.jpg", "/demo/images/mn10.jpg",
        "/demo/images/mn11.jpg", "/demo/images/mn12.jpg",
        "/demo/images/mn13.jpg"
    ];
    var i = 0;
    var content = document.getElementById("content");
    // content.style.background = "url(/images/mn2.jpg)"; //设置图片的初始图片为该路径的图片
    function changeImages() {
        i++;
        i = i % 5;// 超过5则取余数，保证循环在1,2,3,4,5之间
        content.style.background = "url(" + img[i] + ") no-repeat";
        content.style.backgroundSize = "100% 100%";
    }
    // setInterval(changeImages,5000);
}


// 动画的开始和暂停
function animation() {
    let animationPlayState = document.getElementById("content").style.animationPlayState;
    if (animationPlayState == 'paused') {
        document.getElementById("content").style.animationPlayState ='running';
        document.getElementById("animation").innerText='动画停';
    }
    if (animationPlayState == 'running' || animationPlayState == '') {
        document.getElementById("content").style.animationPlayState ='paused';
        document.getElementById("animation").innerText='动画开';
    }
    if (animationPlayState=='initial') {
        document.getElementById("content").style.animation = 'fateimg 80s linear infinite';
        document.getElementById("animation").innerText='动画停';
    }
}

//手动切换聊天背景图片
var imgs= [
    "/demo/images/mn1.jpg", "/demo/images/mn2.jpg",
    "/demo/images/mn3.jpg", "/demo/images/mn4.jpg",
    "/demo/images/mn5.jpg", "/demo/images/mn6.jpg",
    "/demo/images/mn7.jpg", "/demo/images/mn8.jpg",
    "/demo/images/mn9.jpg", "/demo/images/mn10.jpg",
    "/demo/images/mn11.jpg", "/demo/images/mn12.jpg",
    "/demo/images/mn13.jpg"
    ];
var i = 0;
function changeImage() {
    var content = document.getElementById("content");
    // 移除css 某个属性
    $('#content').css('animation','initial');
    i++;
    i = i % 13;// 超过5则取余数，保证循环在1,2,3,4,5之间
    content.style.background = "url(" + imgs[i] + ") no-repeat";
    content.style.backgroundSize = "100% 100%";
}

// 去到音乐管理后台
function topage(type) {
    if (type==1) {
        window.location.href = '/demo/jsp/music/music_manage.html?username='+$.getUrlParam("username");
    }
}


// 聊天室主代码start=====>
var url = "ws://" + window.location.host + ":9091/demo/page_room/";
var ws = null;
//加入聊天室
$(document).ready(function () {
    $('#joinRoom').click(function () {
        // 验证用户名的合法性
        var username = document.getElementById("user").value;
        if (ws) {
            alert("你已经在聊天室，不能再加入");
            return;
        }

        ws = new WebSocket(url + username);
        //与服务端建立连接触发
        ws.onopen = function () {
            console.log("与服务器成功建立连接")
        };
        //服务端推送消息来时触发
        ws.onmessage = function (ev) {
            talking(ev.data);
        };

        //发生错误触发
        ws.onerror = function () {
            console.log("连接错误")
        };
        //正常关闭触发
        ws.onclose = function () {
            console.log("连接关闭");
        };
    });
})


//退出聊天室
function exitRoom() {
    closeWebSocket();
}

function sendMsg() {
    if (!ws) {
        alert("你已掉线，请重新加入");
        return;
    }
    //消息发送
    var msg = document.getElementById("sendMsg").value;
    if (!msg.trim()) {
        alert("不可以发送空的消息!");
        return;
    }
    ws.send(msg);
    document.getElementById("sendMsg").value = "";


}

function closeWebSocket() {
    if (ws) {
        ws.close();
        ws = null;
        alert("您已离开聊天室");
    }
}

function talking(content) {
    if ("more than 3" == content) {
        ws = null;
        alert("对不起,当前在线人数已经超出上限");
        return;
    }
    document.getElementById("content").append(content + "\r\n");
}

document.onkeydown = cdk;

function cdk() {
    if (event.keyCode == 13) {
        sendMsg();
    }
}

// 心跳检测
window.setInterval(function () {
    ws.send('ping');
}, 55000)
// 聊天室主代码end=====>
