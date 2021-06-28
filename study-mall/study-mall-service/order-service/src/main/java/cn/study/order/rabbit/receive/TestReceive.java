package cn.study.order.rabbit.receive;

import cn.study.order.entity.OrderEntity;
import cn.study.order.entity.OrderReturnReasonEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = {"hello-java-queue"})
public class TestReceive {

    /**
     * queues：声明需要监听的队列
     * 消息类型为 org.springframework.amqp.core.Message
     *
     * 参数可以设置为以下内容
     * 1. Message message: 原生消息，包含消息头+消息体
     * 2. T t: 发送消息的类型，OrderReturnReasonEntity content
     * 3. Channel channel：当前传输数据的通道
     *
     * Queue：可以很多人来监听。只要收到消息，队列删除消息，只有一个人能收到此消息
     *  1） 订单服务启动多个,同一个消息，只能有一个客户端收到
     *  2) 只有一个消息处理完，方法运行结束，才可以接收下一个消息
     *
     */
//    @RabbitListener(queues = {"hello-java-queue"})
    @RabbitHandler
    public void receiveMessage(Message message, OrderReturnReasonEntity content, Channel channel) throws InterruptedException {
        //
        Thread.sleep(3000);
        System.out.println("接收到消息"+message+" ==> 内容" + content);
    }

    @RabbitHandler
    public void receiveMessage(OrderEntity orderEntity) throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("接收到消息"+orderEntity);
    }
}
