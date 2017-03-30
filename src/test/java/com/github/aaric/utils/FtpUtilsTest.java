package com.github.aaric.utils;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * FtpUtils测试类
 * 
 * @author aaric
 * @since 2017-03-23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context-Test.xml" })
public class FtpUtilsTest {

	@Autowired
	private FtpUtils ftpUtils;

	@Test
	public void testUploadFile() throws Exception {
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("logo.png");
		File file = new File(FileUtils.getTempDirectory(), "logo.png");
		FileUtils.writeByteArrayToFile(file, IOUtils.toByteArray(input));
		ftpUtils.uploadFile("test/logo.png", file);
	}

}
