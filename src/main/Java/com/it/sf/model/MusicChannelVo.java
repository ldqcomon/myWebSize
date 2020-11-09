package com.it.sf.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Data
public class MusicChannelVo {
    private Integer id;
    private String name;
    private String channelId;
    private String coverBig;
    private String middleBig;
    private String smallBig;

}