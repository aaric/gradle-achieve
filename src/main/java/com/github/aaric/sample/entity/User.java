package com.github.aaric.sample.entity;

/**
 * 系统-用户类
 * 
 * @author Aaric
 * @since 2017-03-20
 */
public class User {

	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 账户密码
	 */
	private String password;
	/**
	 * 账号状态，1为正常
	 */
	private Integer userStatus;

	public User() {
		super();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

}
