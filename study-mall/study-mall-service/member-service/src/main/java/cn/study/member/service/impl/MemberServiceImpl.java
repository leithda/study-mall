package cn.study.member.service.impl;

import cn.study.member.entity.MemberLevelEntity;
import cn.study.member.entity.vo.MemberRegistVo;
import cn.study.member.execption.PhoneExistsException;
import cn.study.member.execption.UsernameExistsException;
import cn.study.member.service.MemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.study.common.utils.PageUtils;
import cn.study.common.utils.Query;

import cn.study.member.dao.MemberDao;
import cn.study.member.entity.MemberEntity;
import cn.study.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelService memberLevelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(MemberRegistVo vo) {
        MemberEntity memberEntity = new MemberEntity();
        // 设置默认等级
        MemberLevelEntity memberLevelEntity = memberLevelService.getDefaultLevel();
        memberEntity.setLevelId(memberLevelEntity.getId());

        // 检查用户名和手机号是否唯一
        checkPhoneUnique(vo.getPhone());
        checkUsernameUnique(vo.getUsername());

        memberEntity.setMobile(vo.getPhone());
        memberEntity.setUsername(vo.getUsername());

        // 密码,加密存储
        memberEntity.setPassword(null);

        save(memberEntity);

    }

    @Override
    public void checkUsernameUnique(String username) throws UsernameExistsException {
        Integer count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if (count > 0) {
            throw new UsernameExistsException();
        }

    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistsException {
        Integer count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (count > 0) {
            throw new PhoneExistsException();
        }
    }

}