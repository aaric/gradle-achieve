package com.github.aaric.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * FTP文件上传与读取工具类<br>
 * <p>
 * 依赖以下以来jar：<br>
 * <ul>
 * <li>commons-lang.jar</i>
 * <li>commons-io.jar</i>
 * <li>commons-net.jar</i>
 * </ul>
 * </p>
 * 
 * @author Aaric
 * @since 2015-11-02
 */
public class FtpUtils {

	/**
	 * 定义Logger对象
	 */
	private static final Logger logger = Logger.getLogger(FtpUtils.class);

	/**
	 * UTF-8
	 */
	private static final String HTTP_ENCODING_UTF_8 = "UTF-8";
	/**
	 * ISO-8859-1
	 */
	private static final String HTTP_ENCODING_ISO_8859_1 = "ISO-8859-1";

	/**
	 * 使用Spring属性注入FTP初始化参数
	 */
	// FTP端口
	private static int serverFtpPort;
	// FTP主机
	private static String serverFtpHost;
	// 访问FTP用户名
	private static String accountFtpUsername;
	// 访问FTP密码
	private static String accountFtpPassword;
	// FTP连接超时时间
	private static int serverFtpConnectTimeout;
	// FTP数据传输超时时间
	private static int serverFtpDataTimeout;

	/**
	 * 初始化与FTP建立连接
	 * 
	 * @return
	 * @author Aaric
	 * @since 2015-10-30
	 */
	private FTPClient connect() {
		// 初始化FTPClient对象
		FTPClient ftpClient = null;

		try {
			// 设置通用参数
			ftpClient = new FTPClient();
			ftpClient.connect(serverFtpHost, serverFtpPort);
			ftpClient.login(accountFtpUsername, accountFtpPassword);
			ftpClient.setControlEncoding(HTTP_ENCODING_UTF_8);
			ftpClient.setConnectTimeout(serverFtpConnectTimeout);
			ftpClient.setDataTimeout(serverFtpDataTimeout);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();

			// 如果连接失败则关闭连接
			if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				// 连接成功
				logger.info("连接FTP服务器成功...");
			} else {
				// 连接失败
				logger.info("连接FTP服务器失败...");
				ftpClient.disconnect();
				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return ftpClient;
	}

	/**
	 * 获得ISO_8859_1编码字符串，解决FTP中文乱码问题
	 * 
	 * @param string
	 *            字符串
	 * @throws UnsupportedEncodingException
	 * @author Aaric
	 * @since 2015-11-02
	 */
	private static String getStringForISO_8859_1(String string) throws UnsupportedEncodingException {
		return new String(string.getBytes(), HTTP_ENCODING_ISO_8859_1);
	}

	/**
	 * 检查是否已经存在该路径文件
	 * 
	 * @param remotePath
	 *            远程FTP文件相对路径，例如“/uploads/test/test.txt”
	 * @return 默认返回true，文件已经存在
	 * @author Aaric
	 * @since 2015-11-02
	 */
	public boolean isHasFile(String remotePath) {
		// 定义是否成功切换工作目录变量
		boolean change = false;
		// 创建FTPClient对象
		FTPClient ftpClient = connect();

		try {
			// 基本判断
			if (StringUtils.isNotBlank(remotePath)) {
				// 获得查询工作目录名称
				String queryPathName = remotePath.substring(0, remotePath.lastIndexOf("/") + 1);
				// System.out.println(queryPathName);

				// 先判断目录是否存在，再判断文件是否存在
				change = ftpClient.changeWorkingDirectory(getStringForISO_8859_1(queryPathName));
				if (!change) {
					// 目录不存在，故文件不存在
					return false;
				} else {
					// 获得查询文件名称
					String queryFileName = remotePath.substring(remotePath.lastIndexOf("/") + 1);
					// System.out.println(queryFileName);
					// 遍历该目录的所有文件，检查是否存在文件
					// flag: true->存在该文件 | false->不存在该文件
					boolean flag = false;
					for (FTPFile ftpFile : ftpClient.listFiles()) {
						// System.out.println(queryFileName + "|" +
						// ftpFile.getName());
						// 比较查询的文件名字和遍历查询得到的文件名称
						if (StringUtils.equals(queryFileName, ftpFile.getName())) {
							flag = true;
							break;
						}
					}
					return flag;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				// 断开连接，释放连接资源
				ftpClient.disconnect();
				logger.info("关闭FTP连接...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * 上传文件到FTP工作目录
	 * 
	 * @param ftpClient
	 *            FTP对象
	 * @param remotePath
	 *            上传到FTP相对路径，例如“/uploads/test/test.txt”，其中“/uploads”必须已经存在，
	 *            否则因权限问题失败
	 * @param uploadFile
	 *            需要上传到FTP的本地文件
	 * @return 默认返回false，上传文件失败
	 * @author Aaric
	 * @since 2015-11-02
	 */
	private static boolean uploadFile(FTPClient ftpClient, String remotePath, File uploadFile) {
		// 定义是否成功切换工作目录变量
		boolean change = false;
		// 一级一级目录名字
		String[] parents = null;

		try {
			// 切换并级联创建目录
			if (StringUtils.isNotBlank(remotePath)) {
				// 定义是否成功创建目录的变量
				boolean flag = false;
				// 获得一级一级目录名字
				parents = StringUtils.split(remotePath, "/");

				// 判断目录的层级，分别进行操作
				if (1 != parents.length) {
					// 拼接目录信息
					String join = "";
					// 级联创建目录，忽略文件名字(length-1)
					for (int i = 0; i < parents.length - 1; i++) {
						// 拼接工作目录
						join += "/" + parents[i];
						// 切换到工作目录
						change = ftpClient.changeWorkingDirectory(getStringForISO_8859_1(join));
						// 如果目录不存在，则创建该目录
						if (!change) {
							// 创建目录
							flag = ftpClient.makeDirectory(getStringForISO_8859_1(parents[i]));
							if (flag) {
								// 如果成功创建该目录，则第二次切换到该目录
								change = ftpClient.changeWorkingDirectory(getStringForISO_8859_1(join));
							} else {
								// 记录日志
								logger.info("创建目录" + join + "失败...");
							}
						}
					}

				} else {
					// 直接切换到根目录("/")
					change = ftpClient.changeWorkingDirectory("/");
					logger.info("直接切换到根目录\"/\"...");

				}

			}

			// 切换到指定文件目录并上传文件到目录
			if (change) {
				// 定义是否成功上传文件的变量
				boolean upload = false;
				// 获得上传文件流对象
				InputStream input = FileUtils.openInputStream(uploadFile);
				if (null != input) {
					// 远程文件名字为“parents[parents.length - 1]”字符串
					upload = ftpClient.storeFile(getStringForISO_8859_1(parents[parents.length - 1]), input);
					// 关闭文件流
					input.close();
					// 记录日志
					if (upload) {
						logger.info("将\"" + uploadFile.getAbsolutePath() + "\"文件上传到FTP的\"" + remotePath + "\"目录成功...");
					} else {
						logger.info("将\"" + uploadFile.getAbsolutePath() + "\"文件上传到FTP的\"" + remotePath + "\"目录失败...");
					}
				}

				return upload;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return false;
	}

	/**
	 * 上传文件到FTP工作目录
	 * 
	 * @param remotePath
	 *            上传到FTP相对路径，例如“/uploads/test/test.txt”，其中“/uploads”必须已经存在，
	 *            否则因权限问题失败
	 * @param uploadFile
	 *            需要上传到FTP的本地文件
	 * @return 默认返回false，上传文件失败
	 * @author Aaric
	 * @since 2015-11-02
	 */
	public boolean uploadFile(String remotePath, File uploadFile) {
		// 创建FTPClient对象
		boolean returnValue = false;
		FTPClient ftpClient = connect();

		try {
			// 上传文件
			returnValue = uploadFile(ftpClient, remotePath, uploadFile);

			// 断开连接，释放连接资源
			ftpClient.disconnect();
			logger.info("关闭FTP连接...");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	/**
	 * 上传文件到FTP工作目录
	 * 
	 * @param mapUploadFiles
	 *            需要上传到FTP的本地文件，键名为指定FTP相对路径，键值为上传文件
	 * @author Aaric
	 * @since 2015-12-12
	 */
	public void uploadFiles(Map<String, File> mapUploadFiles) {
		// 创建FTPClient对象
		FTPClient ftpClient = connect();

		try {
			// 上传文件
			if (null != mapUploadFiles && 0 != mapUploadFiles.size()) {
				Entry<String, File> entry = null;
				Iterator<Entry<String, File>> it = mapUploadFiles.entrySet().iterator();
				while (it.hasNext()) {
					entry = it.next();
					uploadFile(ftpClient, entry.getKey(), entry.getValue());
				}
			}

			// 断开连接，释放连接资源
			ftpClient.disconnect();
			logger.info("关闭FTP连接...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得指定远程ftp目录下面的文件列表
	 * 
	 * @param remotePath
	 *            远程FTP文件相对路径，例如“/uploads/test/”
	 * @return
	 * @author Aaric
	 * @since 2016-05-04
	 */
	public Set<String> listFiles(String remotePath) {
		// 定义是否成功切换工作目录变量
		boolean change = false;
		// 创建FTPClient对象
		FTPClient ftpClient = connect();

		try {
			// 基本判断
			if (StringUtils.isNotBlank(remotePath)) {
				// 获得查询工作目录名称
				String queryPathName = remotePath.substring(0, remotePath.lastIndexOf("/") + 1);
				//System.out.println(queryPathName);

				// 先判断目录是否存在，再判断文件是否存在
				change = ftpClient.changeWorkingDirectory(getStringForISO_8859_1(queryPathName));
				if (change) {
					// 获得查询文件名称
					Set<String> fileNames = new HashSet<String>();
					for (FTPFile ftpFile : ftpClient.listFiles()) {
						// 将扫描的文件放到列表中
						fileNames.add(ftpFile.getName());
					}
					return fileNames;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				// 断开连接，释放连接资源
				ftpClient.disconnect();
				logger.info("关闭FTP连接...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static int getServerFtpPort() {
		return serverFtpPort;
	}

	public static void setServerFtpPort(int serverFtpPort) {
		FtpUtils.serverFtpPort = serverFtpPort;
	}

	public static String getServerFtpHost() {
		return serverFtpHost;
	}

	public static void setServerFtpHost(String serverFtpHost) {
		FtpUtils.serverFtpHost = serverFtpHost;
	}

	public static String getAccountFtpUsername() {
		return accountFtpUsername;
	}

	public static void setAccountFtpUsername(String accountFtpUsername) {
		FtpUtils.accountFtpUsername = accountFtpUsername;
	}

	public static String getAccountFtpPassword() {
		return accountFtpPassword;
	}

	public static void setAccountFtpPassword(String accountFtpPassword) {
		FtpUtils.accountFtpPassword = accountFtpPassword;
	}

	public static int getServerFtpConnectTimeout() {
		return serverFtpConnectTimeout;
	}

	public static void setServerFtpConnectTimeout(int serverFtpConnectTimeout) {
		FtpUtils.serverFtpConnectTimeout = serverFtpConnectTimeout;
	}

	public static int getServerFtpDataTimeout() {
		return serverFtpDataTimeout;
	}

	public static void setServerFtpDataTimeout(int serverFtpDataTimeout) {
		FtpUtils.serverFtpDataTimeout = serverFtpDataTimeout;
	}

}
