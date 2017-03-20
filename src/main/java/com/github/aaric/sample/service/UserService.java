package com.github.aaric.sample.service;

import com.github.aaric.sample.entity.User;

/**
 * 用户Service接口类
 * 
 * @author Aaric
 * @since 2017-03-20
 */
public interface UserService {

	/**
	 * 根据用户ID获得用户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public User getUserById(Long userId) throws Exception;

}
