package com.github.aaric.dubbo.service.impl;

import org.springframework.stereotype.Component;

import com.github.aaric.dubbo.service.TestDubboService;

/**
 * 测试Dubbo服务接口实现类
 * 
 * @author Aaric
 * @since 2017-03-30
 */
@Component("testDubboService")
public class TestDubboServiceImpl implements TestDubboService {

	@Override
	public String getHelloString() throws Exception {
		return "hello";
	}

}
