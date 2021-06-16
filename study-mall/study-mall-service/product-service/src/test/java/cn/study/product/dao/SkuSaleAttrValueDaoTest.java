package cn.study.product.dao;

import cn.study.product.entity.vo.SkuItemVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SkuSaleAttrValueDaoTest {

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Test
    public void getSaleAttrsBySpuId() {
        List<SkuItemVo.SkuItemSaleAttrsVo> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(9L);
        System.out.println(saleAttrsBySpuId);
    }
}