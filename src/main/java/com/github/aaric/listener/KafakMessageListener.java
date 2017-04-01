package com.github.aaric.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

/**
 * Kafak消息监听器
 * 
 * @author Aaric
 * @since 2017-04-01
 */
public class KafakMessageListener implements MessageListener<Integer, String> {

	@Override
	public void onMessage(ConsumerRecord<Integer, String> data) {
		System.err.println("Kafka: " + data.value());
	}

}
