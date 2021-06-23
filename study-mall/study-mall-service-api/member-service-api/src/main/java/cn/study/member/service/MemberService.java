package cn.study.member.service;

import cn.study.member.entity.vo.MemberLoginVo;
import cn.study.member.entity.vo.MemberRegistVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 23:06:34
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 注册会员
     * @param vo 注册参数
     */
    void regist(MemberRegistVo vo);

    /**
     * 检查用户名是否唯一
     * @param username 用户名
     */
    void checkUsernameUnique(String username);

    /**
     * 检查手机号是否唯一
     * @param phone 手机号
     */
    void checkPhoneUnique(String phone);

    /**
     * 登录
     * @param vo 参数
     */
    MemberEntity login(MemberLoginVo vo);
}

