package cn.study.order.service.impl;

import cn.study.common.utils.PageUtils;
import cn.study.common.utils.Query;
import cn.study.order.dao.OrderDao;
import cn.study.order.entity.OrderEntity;
import cn.study.order.entity.vo.MemberAddressVo;
import cn.study.order.entity.vo.OrderConfirmVo;
import cn.study.order.entity.vo.OrderItemVo;
import cn.study.order.feign.CartFeignService;
import cn.study.order.feign.MemberFeignService;
import cn.study.order.interceptor.LoginUserInterceptor;
import cn.study.order.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


@Service("orderService")
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    CartFeignService cartFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        LinkedHashMap loginUser = LoginUserInterceptor.threadLocal.get();
        Long userId = Long.parseLong(loginUser.get("id")+"");
        log.info("???????????? {}",Thread.currentThread().getName());

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();


        // 1??????????????????????????????????????????
        CompletableFuture<Void> addrFuture = CompletableFuture.runAsync(()->{
            log.info("member????????? {}",Thread.currentThread().getName());
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> address = memberFeignService.getAddress(userId);
            orderConfirmVo.setAddress(address);
        },executor);


        CompletableFuture<Void> cartFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            log.info("cart????????? {}",Thread.currentThread().getName());
            // 2??????????????????????????????????????????
            List<OrderItemVo> items = cartFeignService.currentUserCartItems();
            orderConfirmVo.setItems(items);
        }, executor);


        // feign ???????????????????????????????????????????????????????????????
        /* #SynchronousMethodHandler.targetRequest
        for (RequestInterceptor interceptor : requestInterceptors) {
          interceptor.apply(template);
        }
        * */

        // 3???????????????????????????
        Integer integration = (Integer) loginUser.get("integration");
        orderConfirmVo.setIntegration(integration);

        // 4??????????????????????????????
        CompletableFuture.allOf(addrFuture,cartFuture).get();

        return orderConfirmVo;
    }

}