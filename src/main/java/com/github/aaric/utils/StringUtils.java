package com.github.aaric.utils;

/**
 * 字符串工具栏
 * 
 * @author aaric
 * @since 2017-03-20
 */
public class StringUtils {

	/**
	 * 转换为UTF8字符串
	 * 
	 * @param str
	 *            iso字符串
	 * @return
	 * @throws Exception
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public static String convertISO_8859_1ToUTF_8String(String str) throws Exception {
		return new String(str.getBytes("ISO_8859_1"), "UTF-8");
	}

}
