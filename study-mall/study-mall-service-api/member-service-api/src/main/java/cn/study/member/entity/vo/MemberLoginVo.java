package cn.study.member.entity.vo;

import lombok.Data;

@Data
public class MemberLoginVo {
    /** 登录用户名 */
    private String loginacct;

    /** 用户密码 */
    private String password;
}
