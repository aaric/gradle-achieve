package com.github.aaric.sample.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * UserDao测试类
 * 
 * @author Aaric
 * @since 2017-03-20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-Test.xml" })
public class UserDaoTests extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UserDao userDao;

	@Test
	public void testGetUserById() throws Exception {
		System.err.println("-->" + userDao.getUserById(1L));
	}

}
