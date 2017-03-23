package com.github.aaric.utils;

import org.junit.Test;

/**
 * EmailUtils测试类
 * 
 * @author aaric
 * @since 2017-03-23
 */
public class EmailUtilsTest {

	@Test
	public void testSendFromTemplate() throws Exception {
		EmailUtils.sendFromTemplate("测试标题", "匿名用户", "测试", "1669952880@qq.com");
	}

}
