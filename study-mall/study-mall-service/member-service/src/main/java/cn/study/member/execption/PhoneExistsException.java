package cn.study.member.execption;

public class PhoneExistsException extends RuntimeException{
    public PhoneExistsException() {
        super("手机号已存在");
    }
}
