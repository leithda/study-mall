package cn.study.member.dao;

import cn.study.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 23:06:34
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
