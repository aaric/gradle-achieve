package com.github.aaric.utils;

import com.github.aaric.exception.WebsiteException;

/**
 * 异常相关操作
 * 
 * @author Aaric
 * @since 2017-03-20
 */
public class ExceptionUtils {

	/**
	 * 验证条件抛出异常
	 * 
	 * @param condition
	 *            条件
	 * @param errorCode
	 *            异常码
	 * @param data
	 *            对象
	 * @throws Exception
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public static void verifyCondition(boolean condition, String errorCode, Object data) throws Exception {
		if (condition) {
			if (null != data) {
				throw new WebsiteException(errorCode, data);
			} else {
				throw new WebsiteException(errorCode);
			}
		}
	}

	/**
	 * 验证条件抛出异常
	 * 
	 * @param condition
	 *            条件
	 * @param errorCode
	 *            异常码
	 * @throws Exception
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public static void verifyCondition(boolean condition, String errorCode) throws Exception {
		verifyCondition(condition, errorCode, null);
	}

	/**
	 * 验证条件抛出异常
	 * 
	 * @param condition
	 *            条件
	 * @param data
	 *            对象
	 * @throws Exception
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public static void verifyCondition(String errorCode, Object data) throws Exception {
		verifyCondition(true, errorCode, data);
	}

	/**
	 * 验证条件抛出异常
	 * 
	 * @param errorCode
	 *            异常码
	 * @throws Exception
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public static void verifyCondition(String errorCode) throws Exception {
		verifyCondition(true, errorCode);
	}

}