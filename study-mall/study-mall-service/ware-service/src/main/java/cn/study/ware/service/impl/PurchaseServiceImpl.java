package cn.study.ware.service.impl;

import cn.study.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.study.common.utils.PageUtils;
import cn.study.common.utils.Query;

import cn.study.ware.dao.PurchaseDao;
import cn.study.ware.entity.PurchaseEntity;
import cn.study.ware.service.PurchaseService;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(wrapper-> wrapper.eq("purchase_id",key).or().eq("sku_id",key));
        }

        String status = (String) params.get("status");
        if (StringUtils.isNotBlank(status)) {
            queryWrapper.eq("status",status);
        }

        String wareId = (String) params.get("wareId");
        if (StringUtils.isNotBlank(wareId)) {
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}