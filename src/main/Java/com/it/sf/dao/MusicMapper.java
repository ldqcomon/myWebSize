package com.it.sf.dao;

import com.it.sf.model.MusicChannelVo;
import com.it.sf.model.MusicSongVo;
import java.util.List;

public interface MusicMapper {
    List<MusicChannelVo> getMusicChannel(MusicChannelVo musicChannelVo);
    List<MusicSongVo> getMusicSongs(MusicSongVo musicSongVo);
    void addMusicSongs(MusicSongVo musicSongVo);
    MusicSongVo getLyric(MusicSongVo musicSongVo);
}