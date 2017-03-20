package com.github.aaric.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.aaric.abs.AbstractServiceObject;
import com.github.aaric.sample.dao.UserDao;
import com.github.aaric.sample.entity.User;
import com.github.aaric.sample.service.UserService;

/**
 * 用户Service操作实现类
 * 
 * @author Aaric
 * @since 2017-03-20
 */
@Transactional
@Scope("singleton")
@Service("userService")
public class UserServiceImpl extends AbstractServiceObject implements UserService {

	@Autowired
	private UserDao uerDao;
	
	@Override
	public User getUserById(Long userId) throws Exception {
		return uerDao.getUserById(userId);
	}

}
