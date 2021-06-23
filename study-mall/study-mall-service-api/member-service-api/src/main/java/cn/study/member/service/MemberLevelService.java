package cn.study.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.study.common.utils.PageUtils;
import cn.study.member.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 23:06:35
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取默认会员等级
     */
    MemberLevelEntity getDefaultLevel();
}

