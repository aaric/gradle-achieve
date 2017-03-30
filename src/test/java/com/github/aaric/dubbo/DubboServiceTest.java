package com.github.aaric.dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.aaric.dubbo.service.TestDubboService;

/**
 * Dubbo服务测试类
 * 
 * @author Aaric
 * @since 2017-03-30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context-Test.xml", "classpath:spring-dubbo-Test.xml" })
public class DubboServiceTest {

	@Autowired
	@Qualifier("testDubboServiceConsumer")
	private TestDubboService testDubboServiceConsumer;

	@Test
	public void testConsumer() throws Exception {
		System.err.println(testDubboServiceConsumer.getHelloString());
	}

}
