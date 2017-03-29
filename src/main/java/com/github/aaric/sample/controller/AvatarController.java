package com.github.aaric.sample.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.github.aaric.abs.AbstractControllerEntity;
import com.github.aaric.settings.ResponseDefine;
import com.github.aaric.utils.FtpUtils;
import com.github.aaric.utils.ImageUtils;

/**
 * Flash修改头像服务端控制器
 * 
 * @author Aaric
 * @since 2015-12-08
 */
@RestController
@Scope("prototype")
@RequestMapping("/avatar")
public class AvatarController extends AbstractControllerEntity {

	/**
	 * 修改头像-成功
	 */
	private static final String AVATAR_SUCCESS_XML = "<?xml version=\"1.0\" ?><root><face success=\"1\"/></root>";
	/**
	 * 修改头像-失败
	 */
	private static final String AVATAR_FAILURE_XML = "<?xml version=\"1.0\" ?><root><face success=\"0\"/></root>";
	/**
	 * 修改头像-上传临时图片
	 */
	private static final String AVATAR_ACTION_TYPE_UPLOAD = "uploadavatar";
	/**
	 * 修改头像-上传头像
	 */
	private static final String AVATAR_ACTION_TYPE_RECT = "rectavatar";
	/**
	 * 上传FTP图片目录
	 */
	private static final String AVATAR_FTP_DIRECTORY = "/user_image/";
	/**
	 * 上传FTP图片缓存目录
	 */
	private static final String AVATAR_FTP_TEMP_DIRECTORY = "/tmp_image/";

	/**
	 * FTP工具类
	 */
	@Autowired
	private FtpUtils ftpUtils;

	/**
	 * 根据用户ID获得最新的头像信息
	 * 
	 * @return
	 * @throws Exception
	 * @author Aaric
	 * @since 2016-01-04
	 */
	@RequestMapping(value = "/lastest", method = RequestMethod.GET)
	public Map<String, Object> lastest() throws Exception {
		try {
			// 获得用户ID信息
			Long userId = 1L;
			logger.info("-- userId: " + userId);

			// 获得最新的头像信息
			String avatarPath = "test/logo.png";
			setReturnMapData(avatarPath);
			logger.info("-- avatarPath: " + avatarPath);

		} catch (Exception e) {
			// 返回错误信息，打印错误日志
			setReturnMapFail(e);
			logger.error("-- exception: " + ExceptionUtils.getFullStackTrace(e));
		}

		// 打印日志
		logger.info("");

		return returnMap;
	}

	/**
	 * 裁剪手机端上传的图片，并且上传到ftp服务器
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 * @author Aaric
	 * @since 2015-12-22
	 */
	@RequestMapping(value = "/clipping", method = RequestMethod.POST)
	public Map<String, Object> clipping(HttpServletRequest request)
			throws Exception {
		try {
			// 获得用户ID信息
			Long userId = 1L;
			logger.info("-- userId: " + userId);

			// 上传文件
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());

			// 判断是否存在文件信息
			if (multipartResolver.isMultipart(request)) {
				// 读取临时图片
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				MultipartFile avatarFile = multiRequest.getFile("avatar");
				if (null != avatarFile) {
					// 获得文件信息
					System.err.println(avatarFile.getOriginalFilename());
					BufferedImage bufferedImage = ImageIO.read(avatarFile.getInputStream());

					// 存储位置
					String imageType = "jpg";
					String uuidString = UUID.randomUUID().toString();
					File uploadTempBigFile = File.createTempFile(uuidString, "_big.jpg");
					File uploadTempMiddleFile = File.createTempFile(uuidString, "_middle.jpg");
					File uploadTempSmallFile = File.createTempFile(uuidString, "_small.jpg");

					// 大头像
					OutputStream output1 = FileUtils.openOutputStream(uploadTempBigFile);
					ImageIO.write(bufferedImage, imageType, output1);
					output1.close();

					// 中头像
					OutputStream output2 = FileUtils.openOutputStream(uploadTempMiddleFile);
					if (bufferedImage.getWidth() > 120 || bufferedImage.getHeight() > 120) {
						int scaleSize = NumberUtils.max(bufferedImage.getWidth(), bufferedImage.getHeight(), 0);
						ImageIO.write(ImageUtils.scaleTo(bufferedImage, -120.0f / scaleSize), imageType, output2);
					} else {
						ImageIO.write(bufferedImage, imageType, output2);
					}
					output2.close();

					// 小头像
					OutputStream output3 = FileUtils.openOutputStream(uploadTempSmallFile);
					if (bufferedImage.getWidth() > 48 || bufferedImage.getHeight() > 48) {
						int scaleSize = NumberUtils.max(bufferedImage.getWidth(), bufferedImage.getHeight(), 0);
						ImageIO.write(ImageUtils.scaleTo(bufferedImage, -48.0f / scaleSize), imageType, output3);
					} else {
						ImageIO.write(bufferedImage, imageType, output3);
					}
					output3.close();

					// 上传到FTP服务器
					Map<String, File> mapUploadFiles = new HashMap<String, File>();
					String storageAvatarPath = AVATAR_FTP_DIRECTORY + uuidString + ".jpg";
					mapUploadFiles.put(storageAvatarPath, uploadTempBigFile);
					mapUploadFiles.put(AVATAR_FTP_DIRECTORY + uuidString + "_middle.jpg", uploadTempMiddleFile);
					mapUploadFiles.put(AVATAR_FTP_DIRECTORY + uuidString + "_small.jpg", uploadTempSmallFile);
					ftpUtils.uploadFiles(mapUploadFiles);

					// 返回头像地址
					setReturnMapData(storageAvatarPath);

					// 打印日志
					logger.info("-- user_id(" + userId + ") finished modify avatar (" + storageAvatarPath
							+ "), IP: " + getClientIpString());

				} else {
					// 无头像信息
					setReturnMapFail(ResponseDefine.ERORR_NO_AVATAR);
				}
			} else {
				// 无头像信息
				setReturnMapFail(ResponseDefine.ERORR_NO_AVATAR);
			}

		} catch (Exception e) {
			// 返回错误信息，打印错误日志
			setReturnMapFail(e);
			logger.error("-- exception: " + ExceptionUtils.getFullStackTrace(e));
		}

		// 打印日志
		logger.info("");

		return returnMap;
	}

	/**
	 * 设置用户头像<br>
	 * <i>配合camera.swf完成操作</i>
	 * 
	 * @param actionType
	 *            动作信息
	 * @return
	 * @throws Exception
	 * @author Aaric
	 * @since 2015-12-08
	 */
	@RequestMapping(value = "/upload/*", method = { RequestMethod.GET, RequestMethod.POST })
	public String upload(HttpServletRequest request, @RequestParam(value = "a") String actionType,
			@RequestParam("input") String userIdString) throws Exception {
		// 初始化
		// System.err.println("--actionType: " + actionType);

		// 根据actionType字符串，判断SWF图片裁剪的动作
		if (AVATAR_ACTION_TYPE_UPLOAD.equals(actionType)) {
			// 上传临时图片
			String basePathd = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
			String imageURI = basePathd + "/resource/user_image/avatar37.jpg";
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			// 判断是否存在文件信息
			if (multipartResolver.isMultipart(request)) {
				// 读取临时图片
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				MultipartFile imageFile = multiRequest.getFile("Filedata");
				if (null != imageFile) {
					// 获得文件信息
					String uuidString = UUID.randomUUID().toString();
					String fileName = imageFile.getOriginalFilename();
					String uniqueFileName = uuidString + fileName.substring(fileName.lastIndexOf("."));

					// 保存文件
					File uploadImageFile = File.createTempFile(uuidString, uniqueFileName);
					imageFile.transferTo(uploadImageFile);
					ftpUtils.uploadFile(AVATAR_FTP_TEMP_DIRECTORY + uniqueFileName, uploadImageFile);

					// 获得图片网页路径-非本地与LAN环境
					imageURI = basePathd + "/resource" + AVATAR_FTP_TEMP_DIRECTORY + uniqueFileName;
					logger.warn("Flash Temp imageURI: " + imageURI);

					/*** 本地开发环境特殊处理 ***/
					String serverName = request.getServerName();
					if (serverName.startsWith("localhost") || serverName.startsWith("127")) {
						// 测试环境配置
						String imageBasePath = basePathd + request.getContextPath() + "/avatar/";
						String imageStoragePath = getServletContext().getRealPath("avatar");

						// 特殊处理地址
						FileUtils.copyFile(uploadImageFile, new File(imageStoragePath, uniqueFileName));
						imageURI = imageBasePath + uniqueFileName;
					}
					/*** 本地开发环境特殊处理 ***/

				}
			}

			return imageURI;

		} else if (AVATAR_ACTION_TYPE_RECT.equals(actionType)) {
			/* 上传头像 */
			String avatar1 = request.getParameter("avatar1");
			String avatar2 = request.getParameter("avatar2");
			String avatar3 = request.getParameter("avatar3");

			/* 从字符串获得文件流，然后保存图片信息 */
			String uuidString = UUID.randomUUID().toString();

			/* 上传到FTP服务器 */
			File uploadTempBigFile = File.createTempFile(uuidString, "_big.jpg");
			File uploadTempMiddleFile = File.createTempFile(uuidString, "_middle.jpg");
			File uploadTempSmallFile = File.createTempFile(uuidString, "_small.jpg");

			// 生成头像-大中小
			FileUtils.writeByteArrayToFile(uploadTempBigFile, getFlashDataBytes(avatar1));
			FileUtils.writeByteArrayToFile(uploadTempMiddleFile, getFlashDataBytes(avatar2));
			FileUtils.writeByteArrayToFile(uploadTempSmallFile, getFlashDataBytes(avatar3));

			// 上传到FTP服务器
			Map<String, File> mapUploadFiles = new HashMap<String, File>();
			String storageAvatarPath = AVATAR_FTP_DIRECTORY + uuidString + ".jpg";
			mapUploadFiles.put(storageAvatarPath, uploadTempBigFile);
			mapUploadFiles.put(AVATAR_FTP_DIRECTORY + uuidString + "_middle.jpg", uploadTempMiddleFile);
			mapUploadFiles.put(AVATAR_FTP_DIRECTORY + uuidString + "_small.jpg", uploadTempSmallFile);
			ftpUtils.uploadFiles(mapUploadFiles);

			// 保存图片到数据库
			if (StringUtils.isNotBlank(userIdString)) {
				// 获得用户ID信息
				//Integer userId = NumberUtils.createInteger(new String(Base64.decodeBase64(userIdString.getBytes()), Charsets.UTF_8));
				Long userId = 1L;

				// 保存头像到数据库
				//userSafetyService.saveAvatarByUserId(userId, storageAvatarPath);

				// 打印日志
				logger.info(">> /cureuser/userapi/avatar/upload/*");
				logger.info("-- user_id(" + userId + ") finished modify avatar (" + storageAvatarPath + "), IP: " + getClientIpString());
				logger.info("");

			}

			return AVATAR_SUCCESS_XML;
		} else {
			// 其他情况
			return AVATAR_FAILURE_XML;
		}

	}

	/**
	 * 通过字符串或者图片字节数据
	 * 
	 * @param string
	 *            Flash字符串数据
	 * @return
	 * @author Aaric
	 * @since 2015-12-08
	 */
	private byte[] getFlashDataBytes(String string) {
		char[] s = string.toCharArray();
		int len = s.length;
		byte[] bytes = new byte[len / 2];
		for (int i = 0; i < len; i = i + 2) {
			int k1 = s[i] - 48;
			k1 -= k1 > 9 ? 7 : 0;
			int k2 = s[i + 1] - 48;
			k2 -= k2 > 9 ? 7 : 0;
			bytes[i / 2] = (byte) (k1 << 4 | k2);
		}
		return bytes;
	}

}
