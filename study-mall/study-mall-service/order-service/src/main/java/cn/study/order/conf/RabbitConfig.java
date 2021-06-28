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
