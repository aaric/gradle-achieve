package com.github.aaric.abs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.github.aaric.settings.ResponseDefine;

/**
 * 控制器Controller抽象实体类
 * 
 * @author Aaric
 * @since 2017-03-20
 */
public abstract class AbstractControllerEntity extends AbstractControllerObject {

	/**
	 * 初始化ModelAndView对象
	 */
	protected ModelAndView modelAndView;

	/**
	 * 初始化返回MAP对象
	 */
	protected Map<String, Object> returnMap;

	/**
	 * 默认构造函数
	 */
	public AbstractControllerEntity() {
		super();
		modelAndView = new ModelAndView();
		returnMap = new HashMap<String, Object>();
		returnMap.put(ResponseDefine.STATUS, ResponseDefine.SUCCESS);
		returnMap.put(ResponseDefine.MESSAGE, ResponseDefine.REQUEST_SUCCESS);
	}

	/**
	 * 设置成功返回Map对象
	 * 
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public Map<String, Object> setReturnMapSuccess() {
		returnMap.put(ResponseDefine.STATUS, ResponseDefine.SUCCESS);
		returnMap.put(ResponseDefine.MESSAGE, ResponseDefine.REQUEST_SUCCESS);
		return returnMap;
	}

	/**
	 * 设置成功返回Map对象
	 * 
	 * @param successCode
	 *            成功代码
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public Map<String, Object> setReturnMapSuccess(String successCode) {
		returnMap.put(ResponseDefine.STATUS, ResponseDefine.SUCCESS);
		returnMap.put(ResponseDefine.MESSAGE, successCode);
		return returnMap;
	}

	/**
	 * 设置失败返回Map对象
	 * 
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public Map<String, Object> setReturnMapFail() {
		returnMap.put(ResponseDefine.STATUS, ResponseDefine.FAIL);
		returnMap.put(ResponseDefine.MESSAGE, ResponseDefine.ERORR_UNKNOW);
		return returnMap;
	}

	/**
	 * 设置失败返回Map对象
	 * 
	 * @param errorCode
	 *            失败代码
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public Map<String, Object> setReturnMapFail(String errorCode) {
		returnMap.put(ResponseDefine.STATUS, ResponseDefine.FAIL);
		returnMap.put(ResponseDefine.MESSAGE, errorCode);
		return returnMap;
	}

	/**
	 * 设置失败返回Map对象
	 * 
	 * @param e
	 *            异常对象
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public Map<String, Object> setReturnMapFail(Throwable e) {
		returnMap.put(ResponseDefine.STATUS, ResponseDefine.FAIL);
		returnMap.put(ResponseDefine.MESSAGE, ResponseDefine.ERORR_UNKNOW);
		returnMap.put(ResponseDefine.ERROR, e.getMessage());
		return returnMap;
	}

	/**
	 * 设置失败返回Map对象
	 * 
	 * @param errorCode
	 *            失败代码
	 * @param e
	 *            异常对象
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public Map<String, Object> setReturnMapFail(String errorCode, Throwable e) {
		returnMap.put(ResponseDefine.STATUS, ResponseDefine.FAIL);
		returnMap.put(ResponseDefine.MESSAGE, errorCode);
		returnMap.put(ResponseDefine.ERROR, e.getLocalizedMessage());
		return returnMap;
	}

	/**
	 * 设置失败返回Map对象
	 * 
	 * @param errorCode
	 *            失败代码
	 * @param errorMsg
	 *            失败信息
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public Map<String, Object> setReturnMapFail(String errorCode, String errorMsg) {
		returnMap.put(ResponseDefine.STATUS, ResponseDefine.FAIL);
		returnMap.put(ResponseDefine.MESSAGE, errorCode);
		returnMap.put(ResponseDefine.ERROR, errorMsg);
		return returnMap;
	}

	/**
	 * 设置MAP对象DATA域对象
	 * 
	 * @param dataObject
	 *            数据对象
	 * @author Aaric
	 * @since 2017-03-20
	 */
	public Map<String, Object> setReturnMapData(Object dataObject) {
		if (null != dataObject) {
			returnMap.put(ResponseDefine.DATA, dataObject);
		} else {
			returnMap.remove(ResponseDefine.DATA);
		}
		return returnMap;
	}

}
