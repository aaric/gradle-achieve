package com.github.aaric.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * 收/发送邮件工具类
 * 
 * @author Aaric
 * @since 2015-12-24
 */
public class EmailUtils {

	/**
	 * 日志对象
	 */
	private static Logger logger = Logger.getLogger(EmailUtils.class);

	// 接受/发送邮件帐户信息
	private static String EMAIL_ACCOUNT = null;
	private static String EMAIL_PASSWORD = null;

	// 邮件标题与跳转URL
	private static String WEBSITE_BASE_URL = null;
	private static String WEBSITE_EMAIL_TITLE = null;

	// 纯文本类型
	protected static final String TYPE_TEXT = "text/plain;charset=utf-8";
	// HTML文本类型
	private static final String TYPE_HTML = "text/html;charset=utf-8";

	// Java邮件Properties对象
	private static Properties props = null;
	// Java邮件Session对象(单例模式)
	private static Session session = null;

	// one tab space
	protected static final String TABS1_SPACE = "<span style=\"padding-left: 30px;\" />";

	static {
		// 初始化配置信息
		try {
			// 读取属性文件
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("options.properties");
			props = new Properties();
			props.load(is);
			is.close();

			// 设置用户名和密码
			EMAIL_ACCOUNT = props.getProperty("mail.account.username", "account");
			EMAIL_PASSWORD = props.getProperty("mail.account.password", "password");

			// 设置邮件标题与跳转URL
			WEBSITE_BASE_URL = props.getProperty("website.base.url", "http://www.curefun.com");
			WEBSITE_EMAIL_TITLE = props.getProperty("website.mail.title", "测试网站消息中心");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 私有构造函数
	 * 
	 * @author hanql
	 * @since 2015-08-24
	 */
	private EmailUtils() {
		// nothing
	}

	/**
	 * 统一Java邮件Session对象
	 * 
	 * @return
	 * @author hanql
	 * @since 2015-08-24
	 */
	protected static Session getSession() {
		// 单例模式
		if (null == session) {
			// 构建Session对象
			session = Session.getInstance(props, new Authenticator() {

				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// 设置发件人用户名和密码
					return new PasswordAuthentication(EMAIL_ACCOUNT, EMAIL_PASSWORD);
				}

			});
		}
		return session;
	}
	
	/**
	 * 发送简单邮件信息
	 * 
	 * @param subject
	 *            邮件主题
	 * @param personName
	 *            名字
	 * @param content
	 *            文本内容
	 * @param tos
	 *            收件人(多个以","分隔)
	 * @return
	 * @throws Exception
	 * @author hanql
	 * @since 2015-08-24
	 */
	protected static boolean sendFromTemplate(String subject, String personName, String content, String tos) {
		try {
			// 定义MimeMessage对象
			MimeMessage msg = new MimeMessage(getSession());

			// 设置收件人/发件人信息
			msg.setFrom(new InternetAddress(EMAIL_ACCOUNT));
			msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(tos, false));

			/*** ============ 主题和内容 ============ ***/
			// 主题
			msg.setSubject(WEBSITE_EMAIL_TITLE + "-" + subject);
			msg.setSentDate(Calendar.getInstance().getTime());

			// 构建邮件内容
			Multipart mp = new MimeMultipart("related");

			// LOGO
			File logoFile = new File(Thread.currentThread().getContextClassLoader().getResource("logo.png").getFile());
			if (logoFile.exists()) {
				BodyPart logoBodyPart = new MimeBodyPart();
				logoBodyPart.setDataHandler(new DataHandler(new FileDataSource(logoFile)));
				logoBodyPart.setFileName(MimeUtility.encodeText("logo.png"));
				logoBodyPart.setHeader("Content-ID", "<LOGO>");
				mp.addBodyPart(logoBodyPart);
			}

			// 构建HTML文本
			BodyPart htmlBodyPart = new MimeBodyPart();
			String currYear = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
			String htmlContent = "<div style=\"margin: 0 auto; width: 600px; border: 2px solid #007ECD; border-radius: 5px; box-shadow: 3px 3px 5px gray;\">"
					+ "<table style=\"width: 100%; border-collapse: collapse; font-family: Microsoft YaHei, Segoe UI, Tahoma, Arial, Verdana, sans-serif !important;\">"
					+ "<tr style=\"background-color: #007ECD; width: 100%;\"><td><div style=\"margin: 5px; padding: 0 0 0 185px; background: url('cid:LOGO') no-repeat; color: white; font-size: 30px; font-weight: bolder; line-height: 50px;\">"
					+ TABS1_SPACE +  "</div></td></tr>"
					+ "<tr><td style=\"padding: 20px; height: 200px; vertical-align: top; font-size: 14px; line-height: 20px;\">"
					+ "<div>亲爱的" + personName
					+ "：</div>" + "<div style=\"width: 560px;\">" + TABS1_SPACE + "您好！" + content + "</div>"
					+ "<div><div style=\"padding-top: 30px;\">" + TABS1_SPACE + "此致，</div>" + "<div>XX团队敬上！</div>"
					+ "<div><table style=\"font-family: Roboto-Regular,Helvetica,Arial,sans-serif; font-size: 12px; color: #B9B9B9; line-height: 1.5;\"><tbody>"
					+ "<tr><td>此电子邮件地址无法接收回复。要就此提醒向我们提供反馈，请<a href=\"" + WEBSITE_BASE_URL + "/curefun_front/template/aboutSystem/feedback.html\" style=\"text-decoration: none; color: #4285F4;\" target=\"_blank\">点击此处</a>。</td></tr>"
					+ "<tr><td>如需更多信息，请访问 <a href=\"" + WEBSITE_BASE_URL + "/curefun_front/template/aboutSystem/guide.html\" style=\"text-decoration: none; color: #4285F4;\" target=\"_blank\">帮助中心</a>。</td></tr>"
					+ "</tbody></table></div></div></td></tr><tr><div></div></tr>"
					+ "<tr style=\"background-color: #007ECD; width: 100%;\"><td style=\"text-align: center;\"><span style=\"padding-right: 5px; color: white; font-size: 8px; line-height: 20px;\">Copyright©"
					+ currYear + "XX有限公司All Rights Reserved鄂ICP备14000000号</span></td></tr></table></div>";
			htmlBodyPart.setContent(htmlContent, TYPE_HTML);
			mp.addBodyPart(htmlBodyPart);

			// 特殊处理某些不支持正常的发送邮件地址后缀
			if (tos.endsWith("@qq.com")) {
				// 设置HTML内容
				// tos.endsWith("@qq.com")
				msg.setContent(mp);
			} else {
				// 设置纯文本内容
				// tos.endsWith("@163.com") || tos.endsWith("@126.com") || tos.endsWith("@yeah.net")
				String logoImageSrc = "http://curefun.duapp.com/CureFunFront/static/main/img/header/logopng24.png";
				htmlContent = htmlContent.replace("cid:LOGO", logoImageSrc);
				msg.setContent(htmlContent, TYPE_HTML);
			}
			/*** ============ 主题和内容 ============ ***/

			// 发送邮件
			Transport.send(msg);
			return true;

		} catch (Exception e) {
			// 打印日志
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		return false;
	}

}
