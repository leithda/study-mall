package cn.study.member.execption;

public class UsernameExistsException extends RuntimeException{

    public UsernameExistsException() {
        super("用户名已存在");
    }
}
