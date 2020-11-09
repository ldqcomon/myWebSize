//导航菜单
function navList(id) {
    var $obj = $("#nav_dot"), $item = $("#J_nav_" + id);
    $item.addClass("on").parent().removeClass("none").parent().addClass("selected");
    $obj.find("h4").hover(function () {
        $(this).addClass("hover");
    }, function () {
        $(this).removeClass("hover");
    });
    $obj.find("p").hover(function () {
        if ($(this).hasClass("on")) {
            return;
        }
        $(this).addClass("hover");
    }, function () {
        if ($(this).hasClass("on")) {
            return;
        }
        $(this).removeClass("hover");
    });
    $obj.find("h4").click(function () {
        var $div = $(this).siblings(".list-item");
        if ($(this).parent().hasClass("selected")) {
            $div.slideUp(600);
            $(this).parent().removeClass("selected");
        }
        if ($div.is(":hidden")) {
            $("#nav_dot li").find(".list-item").slideUp(600);
            $("#nav_dot li").removeClass("selected");
            $(this).parent().addClass("selected");
            $div.slideDown(600);

        } else {
            $div.slideUp(600);
        }
    });
}

/****表格隔行高亮显示*****/
window.onload = function () {
    oTable = document.getElementById("tab");//找表格
    aTr = document.getElementsByTagName("tr");//找所有的行
    for (i = 0; i < aTr.length; i++) {
        if (i % 2 == 0) {
            aTr[i].style.background = "#fff";
        } else {
            aTr[i].style.background = "#ccc";
        }
        ;
    }
    ;

    //页面加载完成时,从登陆页面获取用户名,设置用户
    /*获取到Url里面的参数*/
    (function ($) {
        $.getUrlParam = function (name) {
            const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            const r = encodeURI(window.location.search).substr(1).match(reg);
            if (r != null) return decodeURI(unescape(r[2]));
            return null;
        }
    })(jQuery)
    let username = $.getUrlParam("username");
    document.getElementById("user").innerText = username;

};

$(function () {
    $(".btn_menu").click(function () {
        var title = document.getElementById("songMG").innerText;
        var url = document.getElementById("songMG").getAttribute("url");
        //var url = $(this).attr("url");
        var isSelect = $("#container").tabs('exists', title);
        if (isSelect) {
            $("#container").tabs('select', title);
            return;
        }
        $("#container").tabs('add', {
            title: title,
            content: CreateContent(url),
            closable: true
        });
    })

    function CreateContent(url) {
        var strHtml = '<iframe src="' + url + '" scrolling="no" frameborder="0" fit="true" style="height:100%;width:100%;min-height:600px;"></iframe>';
        return strHtml;
    }

    $('#tijiao').click(function () {
        let title = $('#title').val();
        let artist = $('#artist').val();
        let album = $('#album').val();
        let channelId = $('#channelId').val();
        let channelType = $('#channelType').val();
        // let fileImage = $('#fileImage').val(); 不行,获取不到值
        let fileImage = $("#fileImage").attr("value");
        let fileVideo = $("#fileVideo").attr("value");
        let lyric = $('#lyric').val();
        let remarks = $('#remarks').val();
        if (!title.trim() || !artist.trim() || !channelId.trim()
            || !channelType.trim() || !fileImage.trim() || !fileVideo.trim() || !lyric.trim()) {
            alert("必填参数不能为空!");
        }
        let data = {
            "title": title, "artist": artist, "album": album,
            "channelId": channelId, "channelType": channelType, "picture": fileImage,
            "url": fileVideo, "lyric": lyric, "remarks": remarks
        };
        $.ajax({
            type: "post",
            dataType: "json",
            data: JSON.stringify(data),
            contentType: "application/json;charset=UTF-8",
            url: "/demo/music/addSongs",
            cache: false,
            success: function (result) {
                if (result.ret) {
                    alert("添加歌曲成功!");
                }
            },
            error: function (result) {
                alert("添加歌曲失败!:" + result.msg);
            }

        });
    })

})

function upload(type) {
    const input = type == "1" ? $('#fileImage')[0] : $('#fileVideo')[0];
    const buttonType = type == "1" ? $('#uploadImage')[0] : $('#uploadVideo')[0];
    if (!input.value) {
        alert("您还没有选择文件,不能上传!");
        return null;
    }
    if (type == "1") {
        buttonType.setAttribute("disabled", "true");
        buttonType.innerText = "上传中";
    } else {
        buttonType.setAttribute("disabled", "true");
        buttonType.innerText = "上传中";
    }
    //图片上传成功后会将图片名称赋值给 value 属性
    if (input.value) {
        //使用 FormData 对象
        var formData = new FormData();
        //将图片对象添加到 files
        formData.append('file', input.files[0]);
        // 文件的类型
        formData.append('fileType', type);
        //使用 ajax 上传图片
        $.ajax({
            url: '/demo/upload/song',
            type: 'POST',
            cache: false,
            data: formData,
            processData: false,
            contentType: false,
            success: function (res) {
                if (res.data.trim()) {
                    if (type == 1) {
                        alert("上传图片成功!!");
                        buttonType.setAttribute("disabled", false);
                        buttonType.innerText = "上传";
                        $("#fileImage").attr("value", res.data);
                        console.log($("#fileImage").attr("value"));
                    } else {
                        alert("上传歌曲成功!!");
                        buttonType.setAttribute("disabled", false);
                        buttonType.innerText = "上传";
                        $("#fileVideo").attr("value", res.data);
                        console.log($("#fileVideo").attr("value"));
                    }

                }
            },
            error: function () {
                alert("上传出错!");
            }
        });
    }
}

// 没有起作用:待看
// function changeButton(type) {
//     if (type == "1") {
//        // document.getElementById("uploadImage").removeAttribute("disabled");
//         document.getElementById("uploadImage").setAttribute("disabled",false);
//     } else {
//         //document.getElementById("uploadVideo").removeAttribute("disabled");
//         document.getElementById("uploadVideo").setAttribute("disabled",false);
//     }
// }


