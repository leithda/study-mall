package cn.study.member.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import cn.study.member.entity.vo.MemberLoginVo;
import cn.study.member.entity.vo.MemberRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.study.member.entity.MemberEntity;
import cn.study.member.service.MemberService;
import cn.study.common.utils.PageUtils;
import cn.study.common.utils.R;



/**
 * 会员
 *
 * @author leithda
 * @email leithda@163.com
 * @date 2021-05-08 23:06:34
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }

    @PostMapping("/regist")
    public R regist(@RequestBody MemberRegistVo vo){
        try {
            memberService.regist(vo);
        }catch (Exception e){
            return R.error(e.getMessage());
        }
        return R.ok();
    }
    
    @PostMapping("login")
    public R login(@RequestBody MemberLoginVo vo){
        MemberEntity memberEntity = memberService.login(vo);
        if(Objects.isNull(memberEntity)){
            return R.error();
        }
        return R.ok().put("data",memberEntity);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
