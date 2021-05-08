package cn.study.member.service;

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
}

