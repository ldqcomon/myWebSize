$(document).ready(function () {
	const height = $(document).height();
	$('.main').css('height',height);
	//$(".tipTimer").trigger("click");

	$('.xiayibu').click(function () {
		let mobile = $('.txtphone').val();
		let verifyCode = $('.txtyzm').val();
		if (!mobile.trim() || !verifyCode.trim()) {
			alert("手机号或验证码不能为空");
			return null;
		}
		let data = {"verifyCode":verifyCode,"mobile":mobile};
		$.ajax({
			type: "post",
			dataType: "json",
			data: JSON.stringify(data),
			contentType: "application/json;charset=UTF-8",
			url: "/demo/index/compareVerifyCode",
			success: function (result) {
				if (result.ret) {
					window.location.href = '/demo/jsp/zhuce1.html?mobile='+mobile+'&verifyCode='+verifyCode;
				} else {
					alert("操作失败,"+result.msg);
				}
			},
			error: function () {
				alert("sorry,the system is error");
			}
		})

	})

	$('.register').click(function () {
		let mobile = getUrlParam("mobile");
		let verifyCode = getUrlParam("verifyCode");
		let mobileCode = $('#mobileCode').val();
		let username = $('#username').val();
		let password = $('#password').val();
		let surePassword = $('#surePassword').val();
		if (!isPoneAvailable(mobile)) {
			alert("手机格式不对");
			return null;
		}
		let data = {"mobileCode":mobileCode,"verifyCode":verifyCode,"mobile":mobile,"username":username,"password":password,"surePassword":surePassword};
		$.ajax({
			type: "post",
			dataType: "json",
			data: JSON.stringify(data),
			contentType: "application/json;charset=UTF-8",
			url: "/demo/index/register",
			success: function (result) {
				if (result.ret) {
					window.location.href = '/demo/jsp/zhuceSucc.html';
				} else {
					alert("注册失败,"+result.msg);
				}
			},
			error: function () {
				alert("sorry,the system is error");
			}
		})
	})

})

let countdown = 60;
function settime(val) {
	if (countdown == 60) {
		console.log("发送短信了...............");
		// 调接口获取短信
		let mobile = getUrlParam("mobile");
		let data = {"mobile":mobile};
		$.ajax({
			type: "post",
			dataType: "json",
			data: JSON.stringify(data),
			contentType: "application/json;charset=UTF-8",
			url: "/demo/index/getMobileCode",
			success: function (result) {
				if (result.ret) {
					console.log("获取短信验证码成功");
				} else {
					alert(result.msg);
					countdown = 300;
				}
			},
			error: function () {
				alert("sorry,the system is error");
			}
		})
	}

	if (countdown == 0) {
		$('#getMobileCode').attr("disabled",false);
		val.innerText = '获取短信验证码';
		countdown =60;

	} else {
		$('#getMobileCode').attr("disabled",true);
		val.innerText = countdown+"秒后可重新发送";
		countdown--;
		setTimeout(function() {
			settime(val)
		},1000)
	}

}
//获取URL参数
function getUrlParam(name) {
	const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	const r = encodeURI(window.location.search).substr(1).match(reg);
	if (r != null) return decodeURI(unescape(r[2])); return null;
}

/*点击刷新验证码*/
function changeCode() {
	var src = "/demo/index/getVerifyCode?" + new Date().getTime(); //加时间戳，防止浏览器利用缓存
	$('#verifyCodeIMG').attr("src", src); //jQuery写法

}

// 判断是否为手机号
function isPoneAvailable(pone) {
	var reg = /^[1][3,4,5,7,8][0-9]{9}$/;
	if (!reg.test(pone)) {
		return false;
	} else {
		return true;
	}
}






