package com.volcengine.rmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.RPCHook;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class RocketMQListener {

    /**
     * 消费者实体对象
     */
    private DefaultMQPushConsumer consumer;

    /**
     * RocketMQ 服务端地址
     */
    private static final String NAMESERVER = "http://rocketmq-cnaieb6ee6df70a1.rocketmq.volces.com:9876";

    /**
     * RocketMQ accessKey
     */
    private static final String ACCESSKEY = "mMYZyopxvQb6OdQIcHmpkBM6";

    /**
     * RocketMQ secretKey
     */
    private static final String SECRETKEY = "dzDUGCDhTEF1MiiMrvBok2Gs";

    /**
     * Topic
     */
    public static final String TOPIC = "test";

    /**
     * Consumer group
     */
    public static final String CONSUMER_GROUP = "GID_test";

    public RocketMQListener() throws MQClientException {
        consumer = new DefaultMQPushConsumer(CONSUMER_GROUP, getAclRPCHook(),
                new AllocateMessageQueueAveragely(), true, "");
        consumer.setNamesrvAddr(NAMESERVER);
        // 消费模式:一个新的订阅组第一次启动从队列的最后位置开始消费 后续再启动接着上次消费的进度开始消费
        //consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 订阅主题和标签
        consumer.subscribe(TOPIC, "*");
        // 注册消费的监听，并在此监听中消费消息，并返回消费的状态信息
        consumer.registerMessageListener((MessageListenerConcurrently)(msgs, context) -> {
            //msgs中只收集同一个topic， 同一个tag，并且key相同的msg
            //会把不同的消息分别放置到不同的队列中
            try {
                for (Message msg : msgs) {
                    String body = new String(msg.getBody(), "utf-8");
                    log.info("接收消息时间={}， 消费者获取消息-主题topic为={}， 消费消息为={}",getTime(), msg.getTopic(), body);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        log.info("---------------RocketMQ consumer listener is successfully started--------------");
    }

    private static RPCHook getAclRPCHook() {
        return new AclClientRPCHook(new SessionCredentials(ACCESSKEY, SECRETKEY));
    }

    private static String getTime() {
        // 创建一个SimpleDateFormat对象，指定输出的时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间
        Date date = new Date();
        // 使用SimpleDateFormat对象将时间格式化为标准格式的字符串
        String time = sdf.format(date);
        return time;
    }
}
