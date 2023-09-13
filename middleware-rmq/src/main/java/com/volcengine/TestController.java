package com.volcengine;

import com.volcengine.rmq.RocketMQService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/rmq")
@Slf4j
public class TestController {

	@Autowired
	private RocketMQService rocketMQService;
	public void setRocketMQService(RocketMQService rocketMQService) {
		this.rocketMQService = rocketMQService;
	}

	@RequestMapping("/sendMsg")
	public String sendMsg(){
		String result = "";
		for (int i = 0; i < 3; i++) {
			try {
				Message msg = new Message("test",
						"SPRINGBOOT_MSG_TEST",
						"Hello world msg".getBytes(RemotingHelper.DEFAULT_CHARSET));
				String body = new String(msg.getBody(), "utf-8");
				SendResult sendResult = rocketMQService.getProducer().send(msg);
				System.out.printf("%s%n", sendResult);
				log.info("发送消息时间={}， 生产者发送消息-主题topic为={}， 发送消息为={}",getTime(), msg.getTopic(), body);
				result += "发送消息时间=" + getTime() + "， 生产者发送消息-主题topic为=" + msg.getTopic() + "， 发送消息为=" + body +"<br>";
			} catch (Exception e) {
				//消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理。
				System.out.println(new Date() + " Send rocketmq message failed.");
				e.printStackTrace();
			}
		}
		return result;
	}

	@RequestMapping("/sendDelayMsg")
	public String sendDelayMsg(@RequestParam("delay") int delay){
		String result = "";
		try {
			Message msg = new Message("test",
					"SPRINGBOOT_DELAY_MSG_TEST",
					"Hello world msg".getBytes(RemotingHelper.DEFAULT_CHARSET));
			String body = new String(msg.getBody(), "utf-8");
			//发送延时消息
			long delayTime = System.currentTimeMillis() + (delay * 1000);
			msg.putUserProperty("__STARTDELIVERTIME", String.valueOf(delayTime));

			SendResult sendResult = rocketMQService.getProducer().send(msg);
			System.out.printf("%s%n", sendResult);
			log.info("发送消息时间={}， 生产者发送消息-主题topic为={}， 发送消息为={}",getTime(), msg.getTopic(), body);
			result = "发送消息时间=" + getTime() + "， 生产者发送消息-主题topic为=" + msg.getTopic() + "， 发送消息为=" + body;
		} catch (Exception e) {
			//消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理。
			System.out.println(new Date() + " Send rocketmq message failed.");
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/sendTimerMsg")
	public String sendTimerMsg(@RequestParam("timer") String timer){
		String result = "";
		try {
			Message msg = new Message("test",
					"SPRINGBOOT_TIMER_MSG_TEST",
					"Hello world msg".getBytes(RemotingHelper.DEFAULT_CHARSET));
			String body = new String(msg.getBody(), "utf-8");
			/**
			 *若需要发送定时消息，则需要设置定时时间，消息将在指定时间进行投递，例如消息将在2021-08-10 18:45:00投递。
			 *定时时间格式为：yyyy-MM-dd HH:mm:ss，若设置的时间戳在当前时间之前，则消息将被立即投递给Consumer。
			 */
			long timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(timer).getTime();
			msg.putUserProperty("__STARTDELIVERTIME", String.valueOf(timeStamp));

			SendResult sendResult = rocketMQService.getProducer().send(msg);
			System.out.printf("%s%n", sendResult);
			log.info("发送消息时间={}， 生产者发送消息-主题topic为={}， 发送消息为={}",getTime(), msg.getTopic(), body);
			result = "发送消息时间=" + getTime() + "， 生产者发送消息-主题topic为=" + msg.getTopic() + "， 发送消息为=" + body;
		} catch (Exception e) {
			//消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理。
			System.out.println(new Date() + " Send rocketmq message failed.");
			e.printStackTrace();
		}
		return result;
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
