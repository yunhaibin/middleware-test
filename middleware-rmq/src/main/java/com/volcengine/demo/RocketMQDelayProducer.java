package com.volcengine.demo;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RocketMQDelayProducer {
    //设置为您在火山引擎消息队列 RocketMQ版控制台上创建的 GID，以及替换为RocketMQ实例的AccessKey ID和AccessKey Secret。
    private static RPCHook getAclRPCHook() {
        return new AclClientRPCHook(new SessionCredentials("mMYZyopxvQb6OdQIcHmpkBM6", "dzDUGCDhTEF1MiiMrvBok2Gs"));
    }

    public static void main(String[] args) throws MQClientException {
        /**
         * 创建 Producer
         * 设置为您从火山引擎消息队列 RocketMQ版控制台获取的接入点信息，类似“http://{INSTANCE_ID}.rocketmq.ivolces.com.com:9876”。
         * 设置RocketMQ实例的AccessKey ID和AccessKey Secret。
         */
        DefaultMQProducer producer = new DefaultMQProducer("GID_test", getAclRPCHook(), true,"");
        producer.setNamesrvAddr("http://rocketmq-cnaieb6ee6df70a1.rocketmq.volces.com:9876");
        //如果 SSL 认证策略设置为仅SSL连接，则通过公网访问实例时必须设置setUseTLS(true)
        //producer.setUseTLS(true);
        producer.start();

        for (int i = 0; i < 1; i++) {
            try {
                Message msg = new Message("test",
                        "DELAY_MSG_TEST",
                        "Hello world msg".getBytes(RemotingHelper.DEFAULT_CHARSET));
                //发送延时消息
                long delayTime = System.currentTimeMillis() + (10 * 1000);
                msg.putUserProperty("__STARTDELIVERTIME", String.valueOf(delayTime));

                /**
                 *若需要发送定时消息，则需要设置定时时间，消息将在指定时间进行投递，例如消息将在2021-08-10 18:45:00投递。
                 *定时时间格式为：yyyy-MM-dd HH:mm:ss，若设置的时间戳在当前时间之前，则消息将被立即投递给Consumer。
                 * long timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-08-10 18:45:00").getTime();
                 * msg.putUserProperty("__STARTDELIVERTIME", String.valueOf(timeStamp));
                 */

                SendResult sendResult = producer.send(msg);
                System.out.printf("%s%n",getTime() + " " + sendResult);
            } catch (Exception e) {
                //消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理。
                System.out.println(new Date() + " Send mq message failed.");
                e.printStackTrace();
            }
        }
        //在应用退出前，销毁Producer对象。
        producer.shutdown();
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