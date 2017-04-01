package com.github.aaric.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * ActiveMQ消息监听器
 * 
 * @author Aaric
 * @since 2017-03-29
 */
public class ActiveMQQueueMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		try {
			System.out.println("ActiveMQ: " + ((TextMessage) message).getText());
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
