package com.github.aaric.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ActiveMQ测试类
 * 
 * @author Aaric
 * @since 2017-03-29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-Test.xml" })
public class ActiveMQTest {

	// 用户名
	private final static String USER_NAME = "admin";
	// 密码
	private final static String PASSWORD = "admin";
	// 地址
	private final static String BROKER_URL = "tcp://172.16.170.129:61616";

	@Test
	public void testProducer() throws Exception {
		// 连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USER_NAME, PASSWORD, BROKER_URL);
		// 连接
		Connection connection = connectionFactory.createConnection();
		connection.start();
		// 会话(接受或者发送消息的线程)
		Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
		// 消息的目的地
		Destination destination = session.createQueue("HelloWorld");
		// 消息生产者
		MessageProducer messageProducer = session.createProducer(destination);
		// 发送消息
		for(int i = 0; i < 10; i++) {
			TextMessage textMessage = session.createTextMessage("发送消息" + i + "...");
			messageProducer.send(textMessage);
		}
		// 提交消息
		session.commit();
		connection.close();
	}

	@Test
	public void testConsumer() throws Exception {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USER_NAME, PASSWORD, BROKER_URL);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("HelloWorld");
		MessageConsumer messageConsumer = session.createConsumer(destination);
		while(true) {
			TextMessage textMessage = (TextMessage) messageConsumer.receive();
			if(null != textMessage) {
				System.out.println("收到的消息：" + textMessage.getText());
			} else {
				break;
			}
		}
	}

}
