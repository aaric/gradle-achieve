package com.github.aaric.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * 图像处理工具类
 * 
 * @author Aaric
 * @since 2015-12-22
 */
public class ImageUtils {

	/**
	 * 旋转图像
	 * 
	 * @param bufferedImage
	 *            图像
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图像
	 */
	public static BufferedImage rotateTo(final BufferedImage bufferedImage, final int degree) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int type = bufferedImage.getColorModel().getTransparency();

		BufferedImage image = new BufferedImage(width, height, type);
		Graphics2D graphics2D = image.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		graphics2D.rotate(Math.toRadians(degree), width / 2, height / 2);
		graphics2D.drawImage(bufferedImage, 0, 0, null);

		try {
			return image;
		} finally {
			if (graphics2D != null) {
				graphics2D.dispose();
			}
		}
	}

	/**
	 * 将图像按照指定的比例缩放:-,缩小，+放大
	 * 
	 * @param bufferedImage
	 *            图像
	 * @param scale
	 *            缩放比例
	 * @return 缩放后的图像
	 */
	public static BufferedImage scaleTo(final BufferedImage bufferedImage, final float scale) {
		if (scale == 0) {
			return bufferedImage;
		}

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		int newWidth = 0;
		int newHeight = 0;

		float newScale = Math.abs(scale);
		if (scale > 0) {
			newWidth = (int) (width * newScale);
			newHeight = (int) (height * newScale);
		} else if (scale < 0) {
			newWidth = (int) (width * newScale);
			newHeight = (int) (height * newScale);
		}

		return resizeTo(bufferedImage, newWidth, newHeight);
	}

	/**
	 * 将图像缩放到指定的宽高大小
	 * 
	 * @param bufferedImage
	 *            图像
	 * @param width
	 *            新的宽度
	 * @param height
	 *            新的高度
	 * @return 缩放后的图像
	 */
	public static BufferedImage resizeTo(final BufferedImage bufferedImage, final int width, final int height) {
		int type = bufferedImage.getColorModel().getTransparency();
		BufferedImage image = new BufferedImage(width, height, type);

		Graphics2D graphics2D = image.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		graphics2D.drawImage(bufferedImage, 0, 0, width, height, 0, 0, bufferedImage.getWidth(),
				bufferedImage.getHeight(), null);

		try {
			return image;
		} finally {
			if (graphics2D != null) {
				graphics2D.dispose();
			}
		}
	}

	/**
	 * 将图像水平翻转
	 * 
	 * @param bufferedImage
	 *            图像
	 * @return 翻转后的图像
	 */
	public static BufferedImage flipTo(final BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int type = bufferedImage.getColorModel().getTransparency();

		BufferedImage image = new BufferedImage(width, height, type);
		Graphics2D graphics2D = image.createGraphics();
		graphics2D.drawImage(bufferedImage, 0, 0, width, height, width, 0, 0, height, null);

		try {
			return image;
		} finally {
			if (graphics2D != null) {
				graphics2D.dispose();
			}
		}
	}

	/**
	 * 获取图片的类型如果是 gif、jpg、png、bmp，以外的类型则返回null
	 * 
	 * @param imageBytes
	 *            图片字节数组
	 * @return 图片类型
	 * @throws java.io.IOException
	 *             IO异常
	 */
	public static String getImageType(final byte[] imageBytes) throws IOException {
		ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
		ImageInputStream imageInput = ImageIO.createImageInputStream(input);
		Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInput);
		String type = null;
		if (iterator.hasNext()) {
			ImageReader reader = iterator.next();
			type = reader.getFormatName().toUpperCase();
		}

		try {
			return type;
		} finally {
			if (imageInput != null) {
				imageInput.close();
			}
		}
	}

	/**
	 * 验证图片大小是否超出指定的尺寸未超出指定大小返回true，超出指定大小则返回false
	 * 
	 * @param imageBytes
	 *            图片字节数组
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @return 验证结果未超出指定大小返回true，超出指定大小则返回false
	 * @throws java.io.IOException
	 *             IO异常
	 */
	public static boolean checkImageSize(final byte[] imageBytes, final int width, final int height)
			throws IOException {
		BufferedImage image = byteToImage(imageBytes);
		int sourceWidth = image.getWidth();
		int sourceHeight = image.getHeight();
		if (sourceWidth > width || sourceHeight > height) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 将图像字节数组转化为BufferedImage图像实例
	 * 
	 * @param imageBytes
	 *            图像字节数组
	 * @return BufferedImage图像实例
	 * @throws java.io.IOException
	 *             IO异常
	 */
	public static BufferedImage byteToImage(final byte[] imageBytes) throws IOException {
		ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
		BufferedImage image = ImageIO.read(input);

		try {
			return image;
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}

	/**
	 * 将BufferedImage持有的图像转化为指定图像格式的字节数组
	 * 
	 * @param bufferedImage
	 *            图像
	 * @param formatName
	 *            图像格式名称
	 * @return 指定图像格式的字节数组
	 * @throws java.io.IOException
	 *             IO异常
	 */
	public static byte[] imageToByte(final BufferedImage bufferedImage, final String formatName) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, formatName, output);

		try {
			return output.toByteArray();
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

}
