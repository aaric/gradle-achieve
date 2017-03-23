package com.github.aaric.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * PinYinUtils测试类
 * 
 * @author Aaric
 * @since 2017-03-20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-Test.xml" })
public class PinYinUtilsTest extends AbstractJUnit4SpringContextTests {

	@Test
	public void testPrint() throws Exception {
		System.err.println(PinYinUtils.getFullSpell("中国"));
	}

}
