package com.github.aaric.utils;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉语拼音工具类
 * 
 * @author Aaric
 * @since 2017-03-20
 */
public class PinYinUtils {

	/**
	 * 章节与数字对应关系 例如：第五章-5
	 */
	public static Map<String, Integer> MAP_SECTION_DATA = null;

	static {
		// 构建章节与数字对应关系对象
		String[] digits = new String[] { "", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十" };
		MAP_SECTION_DATA = new HashMap<String, Integer>();

		// 构建“第一章”到“第九十九章”
		String str = null;
		for (int i = 1; i < 100; i++) {
			str = "第";
			if (10 >= i) {
				str += digits[0 == (i % 10) ? 10 : (i % 10)];
			} else if (20 > i) {
				str += "十" + digits[i % 10];
			} else {
				str += digits[i / 10] + "十" + digits[i % 10];
			}
			str += "章";
			//System.out.println(i + ": " + str);
			MAP_SECTION_DATA.put(str, i);
		}

	}

	/**
	 * 私有构造函数
	 */
	private PinYinUtils() {
		super();
	}

	/**
	 * 获取汉字串拼音首字母，英文字符不变
	 * 
	 * @param chinese
	 *            汉字串
	 * @return 汉语拼音首字母
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public static String getFirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (temp != null) {
						pybf.append(temp[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	/**
	 * 获取汉字串拼音，英文字符不变
	 * 
	 * @param chinese
	 *            汉字串
	 * @return 汉语拼音
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public static String getFullSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString();
	}

}