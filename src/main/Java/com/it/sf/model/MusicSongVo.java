package com.it.sf.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Data
public class MusicSongVo {
    private Integer id;
    // 歌曲id
    private String sid;
    // 歌曲名称
    private String title;
    // 歌曲图片路径
    private String picture;
    // 歌曲文件路径
    private String url;
    // 歌手
    private String artist;
    // 歌词
    private String lyric;
    // 频道
    private String channelId;
    //专辑
    private String album;
    // 频道类型
    private String channelType;
    // 备注
    private String remarks;
    // 创建时间
    private String createTime;

}