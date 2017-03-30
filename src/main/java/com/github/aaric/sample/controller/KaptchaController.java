package com.github.aaric.sample.controller;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.aaric.abs.AbstractControllerEntity;
import com.google.code.kaptcha.Producer;

/**
 * 验证码生产控制类
 * 
 * @author Aaric
 * @since 2017-03-30
 */
@RestController
@Scope("prototype")
@RequestMapping("/kaptcha")
public class KaptchaController extends AbstractControllerEntity {

	/**
	 * 依赖kaptcha验证码图片生成器
	 */
	@Autowired
	protected Producer kaptchaProducer;

	/**
	 * 验证码图片
	 * 
	 * @throws Exception
	 * @author Aaric
	 * @since 2017-03-30
	 */
	@RequestMapping(value = "/image", method = RequestMethod.GET)
	public void image() throws Exception {
		// 设置响应头部
		HttpServletResponse response = getResponse();
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");

		// 随机生成验证码字符串
		String kaptchaText = kaptchaProducer.createText();
		System.out.println(kaptchaText);

		// 缓存到Redis缓存服务器
		//String uuidString = setRedisCacheKeyValue(Constants.KAPTCHA_SESSION_KEY, kaptchaText);

		// 生成验证码图片
		BufferedImage image = kaptchaProducer.createImage(kaptchaText);
		ServletOutputStream output = response.getOutputStream();
		ImageIO.write(image, "jpg", output);
		try {
			output.flush();
		} finally {
			output.close();
		}
	}

}
