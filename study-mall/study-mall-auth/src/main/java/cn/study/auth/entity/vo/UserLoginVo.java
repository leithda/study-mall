package cn.study.auth.entity.vo;

import lombok.Data;

@Data
public class UserLoginVo {
    /** 登录用户名 */
    private String loginacct;

    /** 用户密码 */
    private String password;
}
