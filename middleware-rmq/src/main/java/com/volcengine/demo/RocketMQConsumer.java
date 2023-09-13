package com.volcengine.demo;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.RPCHook;

public class RocketMQConsumer {
    //设置为您在火山引擎消息队列 RocketMQ版控制台上创建的GID,以及替换为RocketMQ实例的AccessKey ID和AccessKey Secret
    private static RPCHook getAclRPCHook() {
        return new AclClientRPCHook(new SessionCredentials("mMYZyopxvQb6OdQIcHmpkBM6", "dzDUGCDhTEF1MiiMrvBok2Gs"));
    }

    public static void main(String[] args) throws MQClientException {
        //设置为您在火山引擎消息队列 RocketMQ版控制台上创建的GID, 以及替换为RocketMQ实例的AccessKey ID和AccessKey Secret。
        //设置为火山引擎消息队列 RocketMQ版实例的接入点。
        // 设置为您在火山引擎消息队列 RocketMQ版控制台上创建的Topic。
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("GID_test", getAclRPCHook(),
                new AllocateMessageQueueAveragely(), true, "");
        consumer.setNamesrvAddr("http://rocketmq-cnaieb6ee6df70a1.rocketmq.volces.com:9876");
        consumer.subscribe("test", "*");
        //如果 SSL 认证策略设置为仅SSL连接，则通过公网访问实例时必须设置setUseTLS(true)
        //consumer.setUseTLS(true);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                for (Message msg : msgs) {
                    String body = null;
                    try {
                        body = new String(msg.getBody(), "utf-8");
                        System.out.printf("Receive New Messages: %s %n", getTime() + " " + body);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.printf("%s%n", "------------------Consumer started------------------");
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