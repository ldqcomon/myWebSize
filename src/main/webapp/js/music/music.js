window.onload = function () {
    //取消body的touchmove默认行为，阻止页面滚动
    document.body.addEventListener('touchmove', function (event) {
        event.preventDefault();
    }, false);

    var musicAudio = document.querySelector('#mic_audio'); // audio标签
    var channel = document.querySelector('.channel'); // 频道标签节点
    var songTitle = document.querySelector('.song-title'); // 歌名标签节点
    var singer = document.querySelector('.singer'); // 歌手标签节点
    var recordImg = document.querySelector('.record-pic img'); // 专辑图片
    var recordPic = document.querySelector('.record-pic'); // 专辑图片外层div节点
    var recordWrap = document.querySelector('.record-wrapper'); // 专辑区域
    var playBtn = document.querySelector('.play'); // 播放按钮
    var nextBtn = document.querySelector('.next'); // 下一区按钮
    var changeChannelBtn = document.querySelector('.change-channel'); // 更换频道
    var modeBtn = document.querySelector('.mode'); // 切换播放模式
    var progressBar = document.querySelector('.progress-bar'); // 进度条外层div
    var progress = document.querySelector('.progress');  // 进度条长度
    var progressBtn = document.querySelector('.progress-btn'); // 进度条拖动按钮
    var lyricBtn = document.querySelector('.show-lyrics'); // 显示歌词按钮
    var lyrics = document.querySelector('.lyrics'); // 歌词的包裹节点
    var bigBg = document.querySelector('.glass img');
    var lyricsLiArr = null;
    let musicUrl="http://106.53.115.110";
    var num = 1;
    // 首次进入页面，chrome,safair在页面没有手动点击的时候，是不能自动播放音乐的，设定num变量标记第一次进入页面
    var channelArr = []; // 用来记录“歌曲频道”的数组
    getChannel();

    // 为播放按钮添加事件
    playBtn.onclick = function () {
        musicAudio.onplaying = null;  //  清除audio标签绑定的事件
        if (musicAudio.paused) {
            playBtn.style.backgroundImage = 'url(/demo/images/music/play.png)';
            musicAudio.play();
        } else {
            playBtn.style.backgroundImage = 'url(/demo/images/music/stop.png)';
            musicAudio.pause();
        }
    };

    // 下一曲按钮
    nextBtn.onclick = function () {
        getMusic();
    };

    // 更换频道按钮
    changeChannelBtn.onclick = function () {
        getRandomChannel(channelArr);
        getMusic();
    };

    // mode按钮
    modeBtn.onclick = function () {
        if (musicAudio.loop) {
            musicAudio.loop = false;
            this.style.backgroundImage = 'url(/demo/images/music/randum.png)';
        } else {
            musicAudio.loop = true;
            this.style.backgroundImage = 'url(/demo/images/music/danquxh.png)';
        }
    };

    // 显示歌词按钮
    lyricBtn.onclick = function () {
        if (recordWrap.style.display == 'block') {
            recordWrap.style.display = 'none';
            channel.style.fontSize = 0;
            if (!lyricsLiArr) {
                getlyric();
            }
        } else {
            recordWrap.style.display = 'block';
            channel.style.fontSize = '0.5rem';
        }
    };



    var isLoading = false;
    var progressTimer = setInterval(activeProgressBar, 300);

    // 激活进度条
    function activeProgressBar () {
        var percentNum = Math.floor((musicAudio.currentTime / musicAudio.duration) * 10000) / 100 + '%';
        progress.style.width = percentNum;
        progressBtn.style.left = percentNum;

        if (percentNum == '100%' && !isLoading && !musicAudio.loop) {
            isLoading = true;
            getMusic();
        }
        if (musicAudio.paused && recordPic.className != 'record-pic mid') {
            recordPic.className = 'record-pic mid';
            playBtn.style.backgroundImage = 'url(/demo/images/music/stop.png)';
            return;
        } else if (recordPic.className != 'record-pic mid rotate' && !musicAudio.paused) {
            recordPic.className = 'record-pic mid rotate';
            playBtn.style.backgroundImage = 'url(/demo/images/music/play.png)';
        }

        // 控制歌词动态滚动
        if (lyricsLiArr) {
            for (var i = 0, len = lyricsLiArr.length-1; i < len; i++) {
                var curT = lyricsLiArr[i].getAttribute('data-time');
                var nexT = lyricsLiArr[i+1].getAttribute('data-time');
                var curtTime = musicAudio.currentTime;
                if ((curtTime > curT) && (curtTime < nexT)) {
                    lyricsLiArr[i].className = 'active';
                    lyrics.style.top = (100 - lyricsLiArr[i].offsetTop) + 'px';
                } else {
                    lyricsLiArr[i].className = '';
                }
            }
        }
    }

    // 进度条操作音乐播放进度，绑定事件
    progressBtn.addEventListener('touchstart', function () {
        clearInterval(progressTimer);
    });
    progressBtn.addEventListener('touchmove', function (e) {
        var percentNum = (e.targetTouches[0].pageX - progressBar.offsetLeft) / progressBar.offsetWidth;
        if (percentNum > 1) {
            percentNum = 1;
        } else if (percentNum < 0){
            percentNum = 0;
        }
        this.style.left = percentNum * 100 + '%';
        progress.style.width = percentNum * 100 + '%';
    });
    progressBtn.addEventListener('touchend', function (e) {
        var percentNum = (e.changedTouches[0].pageX - progressBar.offsetLeft) / progressBar.offsetWidth;
        musicAudio.currentTime = musicAudio.duration * percentNum;
        progressTimer = setInterval(activeProgressBar, 300);
    });

    // 获取频道
    function getChannel () {
        ajax({
            method: 'GET',
            async: false,
            url: '/demo/music/getMusicChannels',
            success: function (response) {
                var jsonObj = JSON.parse(response);
                channelArr = jsonObj['data'];
                getRandomChannel(channelArr);
                getMusic();
            }
        });
    }

    // 获取随机频道
    function getRandomChannel (channelArr) {
        var randomNum = Math.floor(channelArr.length * Math.random());
        var randomChannel = channelArr[randomNum];
        channel.innerHTML = randomChannel.name;
        channel.setAttribute('data-channel-id', randomChannel.channelId);
    }

    // 获取音乐
    function getMusic () {
        const data = {"channelId": channel.getAttribute("data-channel-id")};
        ajax({
            method: 'POST',
            dataType: "json",
            url: '/demo/music/getSongs',
            data: JSON.stringify(data),
            success: function (response) {
                var jsonObj = JSON.parse(response);
                var songObj = jsonObj['data'];
                var randomNum = Math.floor(songObj.length * Math.random());
                var songObj = songObj[randomNum];
                songTitle.innerHTML = songObj.title;
                singer.innerHTML = songObj.artist;
                recordImg.src = musicUrl+songObj.picture;
                bigBg.src = musicUrl+songObj.picture;
                musicAudio.src = musicUrl+songObj.url;
                musicAudio.setAttribute('data-sid', songObj.sid);
                musicAudio.setAttribute('data-ssid', songObj.channelId);
                musicAudio.play();
                isLoading = false;
                getlyric();
                // 解决首次进入页面时，自动播放的兼容问题，不自动播放
                if (num === 1) {
                    musicAudio.onplaying = function () {
                        this.pause();
                        musicAudio.onplaying = null;
                    };
                    num++;
                }
            }
        });
    }
    
    
    function getlyric () {
        var sid = musicAudio.getAttribute('data-sid');
        var ssid = musicAudio.getAttribute('data-ssid');
        let data = {sid: sid, channelId: ssid};
        ajax({
            url: '/demo/music/getLyric',
            method: 'POST',
            data: JSON.stringify(data),
            success: function (response) {
                var lyricsObj = JSON.parse(response);
                lyrics.innerHTML = ''; // 清空歌词
                if (lyricsObj.data.lyric) {
                    var lineArr = lyricsObj.data.lyric.split('\r\n'); // 歌词以排为界数组
                    var timeReg = /\[\d{2}:\d{2}.\d{2}\]/g;
                    var result = [];
                    for (var i in lineArr) {
                        var time = lineArr[i].match(timeReg);
                        if (!time) continue;
                        var curStr = lineArr[i].replace(timeReg, '');
                        for (var j in time) {
                            var t = time[j].slice(1, -1).split(':'); // 时间的格式是[00:00.00] 分钟和毫秒是t[0],t[1]
                            var curSecond = parseInt(t[0], 10) * 60 + parseFloat(t[1]);
                            result.push([curSecond, curStr]);
                        }
                    }
                    result.sort(function (a, b) {
                        return a[0] - b[0];
                    });
                    // 渲染歌词到界面
                    renderLyrics(result);
                }
            }
        })
    }

    // 添加歌词到页面中
    function renderLyrics (lyricArr) {
        var str = '';
        for (var i = 0, len = lyricArr.length; i < len; i++) {
            str += '<li data-time="' + lyricArr[i][0] + '">' + lyricArr[i][1] + '</li>';
        }
        if (lyricArr.length=0) {str='亲,暂无歌词';}
        lyrics.innerHTML = str;
        lyricsLiArr = lyrics.getElementsByTagName('li');
    }
};