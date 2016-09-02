package com.work.common.utils.file;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class ImageTool {

	public static BufferedImage createCataImage(HttpServletRequest request, int width, int height, int codeNums) {
		String codeSequence = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		int eachWidth = width / (codeNums + 1); // 获取每个字占用的宽度
		int fontHeight = height - 2;
		int eachHight = height - 4; // 设置验证码图片中显示的字体高度
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);// 定义验证码图像的缓冲流
		Graphics2D g = buffImg.createGraphics(); // 产生图形上下文
		Random random = new Random(); // 创建随机数产生函数
		g.setColor(Color.WHITE); // 将验证码图像背景填充为白色
		g.fillRect(0, 0, width, height);
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight); // 创建字体格式，字体的大小则根据验证码图片的高度来设定。
		g.setFont(font); // 设置字体。
		g.setColor(Color.PINK); // 为验证码图片画边框，为一个像素。
		g.drawRect(0, 0, width - 1, height - 1);
		g.setColor(Color.BLACK); // 随机生产10跳图片干扰线条，使验证码图片中的字符不被轻易识别
		for (int i = 0; i < 10; i++) {
			int l = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(l, y, l + xl, y + yl);
		}
		StringBuffer randomCode = new StringBuffer(); // randomCode保存随机产生的验证码
		for (int i = 0; i < codeNums; i++) { // 随机生产codeNum个数字验证码
			// 得到随机产生的验证码
			String strRand = String.valueOf(codeSequence.charAt(random.nextInt(codeSequence.length())));
			g.setColor(new Color(random.nextInt(255))); // 用随机产生的颜色将验证码绘制到图像中。
			g.drawString(strRand, (i + 1) * eachWidth, eachHight);
			randomCode.append(strRand); // 将产生的四个随机数组合在一起。
		}
		g.dispose();
		HttpSession session = request.getSession(); // 将生产的验证码保存到Session中
		session.setAttribute("captcha", randomCode.toString());
		return buffImg;
	}

	/**
	 * 图片缩放(图片等比例缩放为指定大小，空白部分以白色填充)
	 * 
	 * @param srcBufferedImage
	 *            源图片
	 * @param destFile
	 *            缩放后的图片文件
	 * @throws IOException
	 */
	public static void zoom(BufferedImage srcBufferedImage, File destFile, int destHeight, int destWidth) throws IOException {
		zoom(srcBufferedImage, destFile, destHeight, destWidth, "JPG");
	}

	public static void zoom(BufferedImage srcBufferedImage, File destFile, int destHeight, int destWidth, String formatName) throws IOException {
		int imgWidth = destWidth;
		int imgHeight = destHeight;
		int srcWidth = srcBufferedImage.getWidth();
		int srcHeight = srcBufferedImage.getHeight();
		if (srcHeight >= srcWidth) {
			imgWidth = (int) Math.round(((destHeight * 1.0 / srcHeight) * srcWidth));
		} else {
			imgHeight = (int) Math.round(((destWidth * 1.0 / srcWidth) * srcHeight));
		}
		BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = destBufferedImage.createGraphics();
		graphics2D.setBackground(Color.WHITE);
		graphics2D.clearRect(0, 0, destWidth, destHeight);
		graphics2D.drawImage(srcBufferedImage.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH), (destWidth / 2) - (imgWidth / 2), (destHeight / 2) - (imgHeight / 2), null);
		graphics2D.dispose();
		ImageIO.write(destBufferedImage, formatName, destFile);
	}

	/**
	 * 添加图片水印
	 * 
	 * @param srcBufferedImage
	 *            需要处理的源图片
	 * @param destFile
	 *            处理后的图片文件
	 * @param watermarkFile
	 *            水印图片文件
	 * @throws IOException
	 * 
	 */
	public static void imageWatermark(BufferedImage srcBufferedImage, File destFile, File watermarkFile, WatermarkPosition watermarkPosition, int alpha) throws IOException {
		int srcWidth = srcBufferedImage.getWidth();
		int srcHeight = srcBufferedImage.getHeight();
		BufferedImage destBufferedImage = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = destBufferedImage.createGraphics();
		graphics2D.setBackground(Color.WHITE);
		graphics2D.clearRect(0, 0, srcWidth, srcHeight);
		graphics2D.drawImage(srcBufferedImage.getScaledInstance(srcWidth, srcHeight, Image.SCALE_SMOOTH), 0, 0, null);

		if (watermarkFile != null && watermarkFile.exists() && watermarkPosition != null && watermarkPosition != WatermarkPosition.no) {
			BufferedImage watermarkBufferedImage = ImageIO.read(watermarkFile);
			int watermarkImageWidth = watermarkBufferedImage.getWidth();
			int watermarkImageHeight = watermarkBufferedImage.getHeight();
			graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha / 100.0F));
			int x = 0;
			int y = 0;
			if (watermarkPosition == WatermarkPosition.topLeft) {
				x = 0;
				y = 0;
			} else if (watermarkPosition == WatermarkPosition.topRight) {
				x = srcWidth - watermarkImageWidth;
				y = 0;
			} else if (watermarkPosition == WatermarkPosition.center) {
				x = (srcWidth - watermarkImageWidth) / 2;
				y = (srcHeight - watermarkImageHeight) / 2;
			} else if (watermarkPosition == WatermarkPosition.bottomLeft) {
				x = 0;
				y = srcHeight - watermarkImageHeight;
			} else if (watermarkPosition == WatermarkPosition.bottomRight) {
				x = srcWidth - watermarkImageWidth;
				y = srcHeight - watermarkImageHeight;
			}
			graphics2D.drawImage(watermarkBufferedImage, x, y, watermarkImageWidth, watermarkImageHeight, null);
		}
		graphics2D.dispose();
		ImageIO.write(destBufferedImage, "JPEG", destFile);
	}

	/**
	 * 图片缩放并添加图片水印(图片等比例缩放为指定大小，空白部分以白色填充)
	 * 
	 * @param srcBufferedImage
	 *            需要处理的图片
	 * @param destFile
	 *            处理后的图片文件
	 * @param watermarkFile
	 *            水印图片文件
	 * @throws IOException
	 * 
	 */
	public static void zoomAndWatermark(BufferedImage srcBufferedImage, File destFile, int destHeight, int destWidth, File watermarkFile, WatermarkPosition watermarkPosition, int alpha)
			throws IOException {
		int imgWidth = destWidth;
		int imgHeight = destHeight;
		int srcWidth = srcBufferedImage.getWidth();
		int srcHeight = srcBufferedImage.getHeight();
		if (srcHeight >= srcWidth) {
			imgWidth = (int) Math.round(((destHeight * 1.0 / srcHeight) * srcWidth));
		} else {
			imgHeight = (int) Math.round(((destWidth * 1.0 / srcWidth) * srcHeight));
		}

		BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = destBufferedImage.createGraphics();
		graphics2D.setBackground(Color.WHITE);
		graphics2D.clearRect(0, 0, destWidth, destHeight);
		graphics2D.drawImage(srcBufferedImage.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH), (destWidth / 2) - (imgWidth / 2), (destHeight / 2) - (imgHeight / 2), null);
		if (watermarkFile != null && watermarkFile.exists() && watermarkPosition != null && watermarkPosition != WatermarkPosition.no) {
			BufferedImage watermarkBufferedImage = ImageIO.read(watermarkFile);
			int watermarkImageWidth = watermarkBufferedImage.getWidth();
			int watermarkImageHeight = watermarkBufferedImage.getHeight();
			graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha / 100.0F));
			int x = 0;
			int y = 0;
			if (watermarkPosition == WatermarkPosition.topLeft) {
				x = 0;
				y = 0;
			} else if (watermarkPosition == WatermarkPosition.topRight) {
				x = destWidth - watermarkImageWidth;
				y = 0;
			} else if (watermarkPosition == WatermarkPosition.center) {
				x = (destWidth - watermarkImageWidth) / 2;
				y = (destHeight - watermarkImageHeight) / 2;
			} else if (watermarkPosition == WatermarkPosition.bottomLeft) {
				x = 0;
				y = destHeight - watermarkImageHeight;
			} else if (watermarkPosition == WatermarkPosition.bottomRight) {
				x = destWidth - watermarkImageWidth;
				y = destHeight - watermarkImageHeight;
			}
			graphics2D.drawImage(watermarkBufferedImage, x, y, watermarkImageWidth, watermarkImageHeight, null);
		}
		graphics2D.dispose();
		ImageIO.write(destBufferedImage, "JPEG", destFile);
	}

	/**
	 * 获取图片文件的真实类型.
	 * 
	 * @param imageFile
	 *            图片文件对象.
	 * @return 图片文件类型
	 * @throws IOException
	 */
	public static String getImageFormatName(File imageFile) throws IOException {
		ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageFile);
		Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
		if (!iterator.hasNext()) {
			return null;
		}
		ImageReader imageReader = (ImageReader) iterator.next();
		imageInputStream.close();
		return imageReader.getFormatName().toLowerCase();
	}

	public static enum WatermarkPosition {
		no, topLeft, topRight, center, bottomLeft, bottomRight
		// 水印位置（无、左上、右上、居中、左下、右下）
	}

	public static void main(String[] args) throws IOException {
		BufferedImage image = ImageIO.read(new File("C:\\Documents and Settings\\User\\桌面\\测试图片\\index_02.gif"));
		zoom(image, new File("C:\\Documents and Settings\\User\\桌面\\测试图片\\small2.png"), 100, 100);
	}
}