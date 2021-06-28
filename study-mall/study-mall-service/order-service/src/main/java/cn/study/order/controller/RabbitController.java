package cn.study.order.controller;

import cn.study.order.entity.OrderEntity;
import cn.study.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@Slf4j
public class RabbitController {


    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("sendMq")
    public String sendMq(@RequestParam(value = "num", defaultValue = "10") Integer num) {
        for (int i = 0; i < num; i++) {
            if (i % 2 == 0) {
                OrderReturnReasonEntity orderReturnReasonEntity = new OrderReturnReasonEntity();
                orderReturnReasonEntity.setId(1L);
                orderReturnReasonEntity.setName("退货原因" + i);
                orderReturnReasonEntity.setCreateTime(new Date());
                orderReturnReasonEntity.setStatus(0);
                orderReturnReasonEntity.setSort(0);
                rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", orderReturnReasonEntity,new CorrelationData(UUID.randomUUID().toString()));
            } else {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setDeliverySn(UUID.randomUUID().toString());
//                rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", orderEntity,new CorrelationData(UUID.randomUUID().toString()));
                // 测试投递错误触发returnCallback
                rabbitTemplate.convertAndSend("hello-java-exchange", "hello22.java", orderEntity,new CorrelationData(UUID.randomUUID().toString()));
            }
        }
        return "ok";
    }
}
