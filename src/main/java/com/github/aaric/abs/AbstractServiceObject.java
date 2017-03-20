package com.github.aaric.abs;

import org.apache.log4j.Logger;

/**
 * 抽象Service抽象类
 * 
 * @author Aaric
 * @since 2017-03-20
 */
public abstract class AbstractServiceObject {

	/**
	 * 初始化日志对象
	 */
	protected Logger logger;

	/**
	 * 默认构造函数
	 */
	public AbstractServiceObject() {
		super();
		logger = Logger.getLogger(this.getClass());
	}

}

