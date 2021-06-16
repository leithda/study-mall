package cn.study.product.dao;


import cn.study.product.entity.vo.SkuItemVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AttrGroupDaoTest {

    @Autowired
    AttrGroupDao attrGroupDao;

    @Test
    public void test(){
        List<SkuItemVo.SpuItemAttrGroupVo> attrGroupWithAttrBySpuId = attrGroupDao.getAttrGroupWithAttrBySpuId(9L, 225L);
        System.out.println(attrGroupWithAttrBySpuId);
    }
}