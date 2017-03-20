package com.github.aaric.abs;

import org.apache.log4j.Logger;

/**
 * 控制器Controller抽象类
 * 
 * @author Aaric
 * @since 2017-03-20
 */
public abstract class AbstractControllerObject {

	/**
	 * 初始化日志对象
	 */
	protected Logger logger;

	/**
	 * 默认构造函数
	 */
	public AbstractControllerObject() {
		super();
		logger = Logger.getLogger(this.getClass());
	}

}
