package com.github.aaric.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * EmailUtils测试类
 * 
 * @author aaric
 * @since 2017-03-23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context-Test.xml" })
public class EmailUtilsTest {

	@Test
	public void testSendFromTemplate() throws Exception {
		EmailUtils.sendFromTemplate("测试标题", "匿名用户", "测试", "1669952880@qq.com");
	}

}
