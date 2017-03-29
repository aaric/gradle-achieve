package com.github.aaric.settings;

/**
 * 公共常量
 * 
 * @author Aaric
 * @since 2017-03-20
 */
public interface ResponseDefine {

	// 请求成功
	public static final String SUCCESS = "success";

	// 请求失败
	public static final String FAIL = "failure";

	// 返回状态
	public static final String STATUS = "status";

	// 错误
	public static final String ERROR = "error";

	// message
	public static final String MESSAGE = "message";

	// 数据
	public static final String DATA = "data";

	// list
	public static final String LIST = "list";

	// 请求成功
	public static final String REQUEST_SUCCESS = "0000";

	// 数据库查询异常
	public static final String ERROR_DB = "0001";

	// 未知错误
	public static final String ERORR_UNKNOW = "0002";

	// 用户ID错误
	public static final String ERROR_USER_ID = "0003";

	// 头像信息错误
	public static final String ERORR_NO_AVATAR = "0004";

}
