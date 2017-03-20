package com.github.aaric.exception;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.aaric.settings.ResponseDefine;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

/**
 * 异常处理器
 * 
 * @author Aaric
 * @since 2017-03-20
 */
@ControllerAdvice
public class ExceptionHandle {

	protected static Logger logger = Logger.getLogger(ExceptionHandle.class);

	@ExceptionHandler(WebsiteException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> handlePatientException(WebsiteException e) {
		logger.error(ExceptionUtils.getFullStackTrace(e)); // 打印错误信息
		Map<String, Object> rspMap = new HashMap<String, Object>();
		rspMap.put(ResponseDefine.STATUS, ResponseDefine.FAIL);
		rspMap.put(ResponseDefine.MESSAGE, e.getErrorCode());
		Object data = e.getData();
		if (null != data) {
			rspMap.put(ResponseDefine.DATA, data);
		} else {
			rspMap.put(ResponseDefine.ERROR, e.getLocalizedMessage());
		}
		return rspMap;
	}

	@ExceptionHandler(SQLException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> handleSQLException(SQLException e) {
		logger.error(ExceptionUtils.getFullStackTrace(e)); // 打印错误信息
		Map<String, Object> rspMap = new HashMap<String, Object>();
		rspMap.put(ResponseDefine.STATUS, ResponseDefine.FAIL);
		rspMap.put(ResponseDefine.MESSAGE, ResponseDefine.ERROR_DB);
		rspMap.put(ResponseDefine.ERROR, e.getLocalizedMessage());
		return rspMap;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> handleException(Exception e) {
		logger.error(ExceptionUtils.getFullStackTrace(e)); // 打印错误信息
		Map<String, Object> rspMap = new HashMap<String, Object>();
		rspMap.put(ResponseDefine.STATUS, ResponseDefine.FAIL);
		rspMap.put(ResponseDefine.MESSAGE, ResponseDefine.ERORR_UNKNOW);
		rspMap.put(ResponseDefine.ERROR, e.getLocalizedMessage());
		return rspMap;
	}

}
