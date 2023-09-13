package com.volcengine.rmq;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
@Getter
@Slf4j
public class RocketMQService {

    /**
     * 生产者实体对象
     */
    private DefaultMQProducer producer;

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
     * Consumer group
     */
    private static final String PRODUCERGROUP = "GID_test";

    public RocketMQService() {
        producer = new DefaultMQProducer(PRODUCERGROUP, getAclRPCHook(), true,"");
        producer.setNamesrvAddr(NAMESERVER);
        start();
        log.info("---------------RocketMQ producer service is successfully started--------------");
    }

    /**
     * 对象在使用之前必须要调用一次，只能初始化一次
     */
    private void start() {
        try {
            this.producer.start();
        } catch (MQClientException e) {
            log.error("---------------RocketMQService is failed--------------");
            e.printStackTrace();
        }
    }

    //设置为您在火山引擎消息队列 RocketMQ版控制台上创建的 GID，以及替换为RocketMQ实例的AccessKey ID和AccessKey Secret。
    private static RPCHook getAclRPCHook() {
        return new AclClientRPCHook(new SessionCredentials(ACCESSKEY, SECRETKEY));
    }

    /**
     * 一般在应用上下文，使用上下文监听器，进行关闭
     */
    public void shutdown() {
        this.producer.shutdown();
    }

}