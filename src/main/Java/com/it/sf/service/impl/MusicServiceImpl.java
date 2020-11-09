package com.it.sf.service.impl;

import com.it.sf.dao.MusicMapper;
import com.it.sf.model.MusicChannelVo;
import com.it.sf.model.MusicSongVo;
import com.it.sf.service.MusicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * @Auther: ldq
 * @Date: 2020/9/21
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
public class MusicServiceImpl implements MusicService {
    @Autowired
    MusicMapper musicMapper;
    @Override
    public List<MusicChannelVo> getMusicChannel(MusicChannelVo musicChannelVo) {
        return musicMapper.getMusicChannel(musicChannelVo);
    }

    @Override
    public List<MusicSongVo> getMusicSongs(MusicSongVo musicSongVo) {
        return musicMapper.getMusicSongs(musicSongVo);
    }

    @Override
    public void addMusicSongs(MusicSongVo musicSongVo) {
        // 随机生成sid
        String sid = RandomStringUtils.randomAlphanumeric(15);
        musicSongVo.setSid(sid);
        musicMapper.addMusicSongs(musicSongVo);
    }

    @Override
    public MusicSongVo getLyric(MusicSongVo musicSongVo) {
        return musicMapper.getLyric(musicSongVo);
    }

    public static void main(String[] args) {
        String sid = RandomStringUtils.randomAlphanumeric(15);
        log.info("sid:{}",sid);

    }
}
