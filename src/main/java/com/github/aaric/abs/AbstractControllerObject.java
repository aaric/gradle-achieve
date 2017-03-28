package com.github.aaric.abs;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.github.aaric.utils.IpUtils;

import org.apache.commons.lang.StringUtils;
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
	 * 注入Web容器ServletContext对象
	 */
	@Autowired
	private ServletContext servletContext;

	/**
	 * 默认构造函数
	 */
	public AbstractControllerObject() {
		super();
		logger = Logger.getLogger(this.getClass());
	}

	/**
	 * 获得ServletContext对象
	 * 
	 * @return
	 * @author Aaric
	 * @since 2015-11-12
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * 获得HttpServletRequest对象
	 * 
	 * @return
	 * @author Aaric
	 * @since 2015-11-12
	 */
	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获得HttpServletResponse对象
	 * 
	 * @return
	 * @author Aaric
	 * @since 2015-12-01
	 */
	public HttpServletResponse getResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}

	/**
	 * 根据Cookie名字获得Cookie的值
	 * 
	 * @param cookieName
	 *            名称
	 * @param cookieValue
	 *            字符串
	 * @return
	 * @author Aaric
	 * @since 2015-12-16
	 */
	public Cookie setCookieValue(String cookieName, String cookieValue) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setPath("/");
		this.getResponse().addCookie(cookie);
		return cookie;
	}

	/**
	 * 根据Cookie名字获得Cookie的值
	 * 
	 * @param cookieName
	 *            名称
	 * @return
	 * @author Aaric
	 * @since 2015-12-16
	 */
	public String getCookieValue(String cookieName) {
		if (StringUtils.isNotBlank(cookieName)) {
			Cookie[] cookies = getRequest().getCookies();
			if (null != cookies && 0 != cookies.length) {
				for (Cookie cookie : cookies) {
					if (cookieName.equals(cookie.getName())) {
						return cookie.getValue();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获得Locale对象(国际化)
	 * 
	 * @return
	 * @author Aaric
	 * @since 2015-11-12
	 */
	public Locale getLocale() {
		Locale returnValue = (Locale) getRequest().getSession()
				.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		if (null == returnValue) {
			returnValue = getRequest().getLocale();
			getRequest().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, returnValue);
		}
		return returnValue;
	}

	/**
	 * 获得客户端真实IP地址字符串
	 * 
	 * @return
	 * @author Aaric
	 * @since 2015-11-12
	 */
	public String getClientIpString() {
		return IpUtils.getClientIpString(getRequest());
	}

}
