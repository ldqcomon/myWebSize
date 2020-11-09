package com.it.sf.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Data
public class UserVo {
    private Integer id;
    private String userCode;
    private String password;
    private String surePassword;
    private String mobile;
    // 验证码
    private String verifyCode;
    // 短信验证码
    private String mobileCode;
    @NotNull
    private String username;
    private String userHeadUrl;
    private Integer status;

}