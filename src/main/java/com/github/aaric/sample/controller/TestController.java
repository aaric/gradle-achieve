package com.github.aaric.sample.controller;

import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.aaric.abs.AbstractControllerEntity;
import com.github.aaric.sample.service.UserService;

/**
 * 测试控制类
 * 
 * @author Aaric
 * @since 2016-02-26
 */
@RestController
@Scope("prototype")
@RequestMapping("/test")
public class TestController extends AbstractControllerEntity {

	/**
	 * 用户Service操作对象
	 */
	@Autowired
	private UserService userService;

	/**
	 * 测试获得数据信息
	 * 
	 * @return
	 * @throws Exception
	 * @author Aaric
	 * @since 2016-02-26
	 */
	@RequestMapping(value = "/getObject", method = RequestMethod.GET)
	public Map<String, Object> getObject() throws Exception {
		try {
			// 获得测试数据
			setReturnMapData(userService.getUserById(1L));

		} catch (Exception e) {
			// 返回错误信息，打印错误日志
			setReturnMapFail(e);
			logger.error("-- exception: " + ExceptionUtils.getFullStackTrace(e));
		}
		return returnMap;
	}

}
