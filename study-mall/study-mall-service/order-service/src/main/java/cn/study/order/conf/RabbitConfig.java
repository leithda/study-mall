package cn.study.order.conf;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RabbitConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 配置RabbitMQ序列化器
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 定制 RabbitTemplate
     * 1、服务器收到消息就回调
     *  spring.rabbitmq.publisher-confirm-type=correlated
     *  设置回调 confirmCallback
     * 2、消息正确抵达队列
     *  1）、spring.rabbitmq.publisher-returns=true
     *      spring.rabbitmq.template.mandatory=true
     *  2）、设置returnCallback回调方法
     *
     * 3、消费端确认机制
     *  1）、默认自动确认，只要接收到消息，服务端会移除消息，客户端会自动确认。收到很多消息，自动回复，只有一个消息处理成功，发送故障，造成消息丢失。需要手动确认
     *      手动确认模式下，只要没给服务端ack响应，消息一直是unacked状态，即使Consumer宕机，消息会重新变更为Ready状态，不会丢失。
     *  2）、如何签收消息
     *      channel.basicAck：签收消息
     *      channel.basicNack：拒签消息
     *
     */
    @PostConstruct // RabbitConfig对象创建完成之后执行
    public void initRabbitTemplate(){
        // 设置消息抵达Exchange确认回调
        /**
         * correlationData： 当前消息的唯一关联数据.消息的唯一ID
         * ack: 消息是否成功收到,只要消息到Exchange，为true
         * cause： 失败原因
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> System.out.println("confirm...correlationData["+correlationData+"],==>ack["+ack+"], ==>cause["+cause+"]"));

        // 设置消息抵达队列的确认回调
        // 消息没有投递给指定的队列触发此回调
        /**
         * message: 投递失败的消息的详细信息
         * replyCode: 回复的状态码
         * replyText: 回复的文本内容
         * exchange: 交换机名
         * routingKey: 路由键
         */
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> System.out.println("失败的消息["+message+"]==> replyCode["+replyCode+"] ==> replyText["+replyText+"] ==> exchange["+exchange+"] ==> routingKey["+routingKey+"]"));
    }
}
