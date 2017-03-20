package com.github.aaric.sample.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.github.aaric.sample.entity.User;

/**
 * 用户DAO接口类
 * 
 * @author Aaric
 * @since 2017-03-20
 */
@Repository
@Scope("singleton")
public interface UserDao {

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
	@Select("SELECT * FROM user WHERE user_id = #{userId} LIMIT 0, 1 ")
	public User getUserById(@Param("userId") Long userId) throws Exception;

}
