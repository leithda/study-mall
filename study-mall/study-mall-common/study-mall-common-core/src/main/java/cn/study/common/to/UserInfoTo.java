package cn.study.common.to;

import lombok.Data;

@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;
    private boolean isTempUser;
}
