package com.it.sf.controller;

import com.it.sf.common.JsonData;
import com.it.sf.model.MusicChannelVo;
import com.it.sf.model.MusicSongVo;
import com.it.sf.service.MusicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Auther: ldq
 * @Date: 2020/9/21
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/music")
@Slf4j
public class MusicController {
    @Autowired
    MusicService musicService;

    @RequestMapping("/getMusicChannels")
    public JsonData getMusicChannels() {
        List<MusicChannelVo> musicChannel = musicService.getMusicChannel(new MusicChannelVo());
        return JsonData.success(musicChannel);
    }

    @RequestMapping("/getSongs")
    public JsonData getSongs(@RequestBody MusicSongVo songVo) {
        List<MusicSongVo> musicSongs = musicService.getMusicSongs(songVo);
        return JsonData.success(musicSongs);
    }

    @RequestMapping("/addSongs")
    public JsonData addSongs(@RequestBody MusicSongVo songVo) {
        try {
            if (StringUtils.isAnyBlank(songVo.getTitle(), songVo.getArtist(),
                    songVo.getChannelId(), songVo.getChannelType(),
                    songVo.getPicture(), songVo.getUrl(), songVo.getLyric())) {
                return JsonData.fail("参数不能为空,请检查必填参数");
            }
            musicService.addMusicSongs(songVo);
            return JsonData.success(null, "添加歌曲成功!");
        } catch (Exception e) {
            log.info("add songs error:{}", e.getMessage());
            return JsonData.fail("添加歌曲失败!");
        }
    }

    @RequestMapping("/getLyric")
    public JsonData getLyric(@RequestBody MusicSongVo songVo) {
        MusicSongVo musicSongs = musicService.getLyric(songVo);
        return JsonData.success(musicSongs);
    }

}
