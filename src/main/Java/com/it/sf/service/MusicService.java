package com.it.sf.service;

import com.it.sf.model.MusicChannelVo;
import com.it.sf.model.MusicSongVo;
import java.util.List;

/**
 * @Auther: ldq
 * @Date: 2020/8/17
 * @Description:
 * @Version: 1.0
 */
public interface MusicService {
    List<MusicChannelVo> getMusicChannel(MusicChannelVo channelVo);
    List<MusicSongVo> getMusicSongs(MusicSongVo musicSongVo);
    void addMusicSongs(MusicSongVo musicSongVo);
    MusicSongVo getLyric(MusicSongVo musicSongVo);
}
