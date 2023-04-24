package com.xjinyao.report.core.expression.function.seal;

import com.itextpdf.xmp.impl.Base64;
import com.xjinyao.report.core.export.pdf.font.FontBuilder;
import lombok.Builder;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * 印章实现
 *
 * @author liwei
 * @createDate 2023-3-2 10:26
 */
@Getter
@Builder
public class Seal {
	/**
	 * 起始位置
	 */
	private static final int INIT_BEGIN = 10;
	public static final int INT_TWO = 2;

	/**
	 * 图片尺寸：宽
	 */
	private Integer width;

	/**
	 * 图片尺寸：高
	 */
	private Integer height;

	/**
	 * 颜色
	 */
	private Color color;

	/**
	 * 主字
	 */
	private SealFont mainFont;

	/**
	 * 副字
	 */
	private SealFont viceFont;

	/**
	 * 抬头
	 */
	private SealFont titleFont;

	/**
	 * 中心字
	 */
	private SealFont centerFont;

	/**
	 * 中心字-签字处
	 */
	private SealFont centerSignFont;

	/**
	 * 边线圆
	 */
	private SealCircle borderCircle;

	/**
	 * 内边线圆
	 */
	private SealCircle borderInnerCircle;

	/**
	 * 内线圆
	 */
	private SealCircle innerCircle;

	/**
	 * 边线框
	 */
	private Integer borderSquare;

	/**
	 * 画公章
	 *
	 * @return 图片base64
	 */
	public String drawOfficialSeal() throws Exception {
		//1.画布
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

		//2.画笔
		Graphics2D g2d = bi.createGraphics();

		//2.1抗锯齿设置
		//文本不抗锯齿，否则圆中心的文字会被拉长
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		//其他图形抗锯齿
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints(hints);

		//2.2设置背景透明度
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1f));

		//2.3填充矩形
		g2d.fillRect(0, 0, width, height);

		//2.4重设透明度，开始画图
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1f));

		//2.5设置画笔颜色
		g2d.setPaint(color == null ? Color.RED : color);

		//3.画边线圆
		if (borderCircle != null) {
			drawCircle(g2d, borderCircle, INIT_BEGIN, INIT_BEGIN);
		} else {
			throw new Exception("BorderCircle can not null！");
		}

		int borderCircleWidth = borderCircle.getWidth();
		int borderCircleHeight = borderCircle.getHeight();

		//4.画内边线圆
		if (borderInnerCircle != null) {
			int x = INIT_BEGIN + borderCircleWidth - borderInnerCircle.getWidth();
			int y = INIT_BEGIN + borderCircleHeight - borderInnerCircle.getHeight();
			drawCircle(g2d, borderInnerCircle, x, y);
		}

		//5.画内环线圆
		if (innerCircle != null) {
			int x = INIT_BEGIN + borderCircleWidth - innerCircle.getWidth();
			int y = INIT_BEGIN + borderCircleHeight - innerCircle.getHeight();
			drawCircle(g2d, innerCircle, x, y);
		}

		//6.画弧形主文字
		if (borderCircleHeight != borderCircleWidth) {
			drawArcFont4Oval(g2d, borderCircle, mainFont, true);
		} else {
			drawArcFont4Circle(g2d, borderCircleHeight, mainFont, true);
		}

		//7.画弧形副文字
		if (borderCircleHeight != borderCircleWidth) {
			drawArcFont4Oval(g2d, borderCircle, viceFont, false);
		} else {
			drawArcFont4Circle(g2d, borderCircleHeight, viceFont, false);
		}

		//8.画中心字
		drawFont(g2d, (borderCircleWidth + INIT_BEGIN) * 2, (borderCircleHeight + INIT_BEGIN) * 2, centerFont);
		//设置画笔颜色
		g2d.setPaint(Color.black);
		//8.1画中心字-黑色签字
		drawFont(g2d, (borderCircleWidth + INIT_BEGIN) * 2, (borderCircleHeight + INIT_BEGIN) * 2, centerSignFont);

		//设置画笔颜色
		g2d.setPaint(color == null ? Color.RED : color);
		//9.画抬头文字
		drawFont(g2d, (borderCircleWidth + INIT_BEGIN) * 2, (borderCircleHeight + INIT_BEGIN) * 2, titleFont);

		g2d.dispose();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bi, "PNG", outputStream);
		String base64Img = new String(Base64.encode(outputStream.toByteArray()));
		base64Img = "data:image/png;base64," + base64Img;
		outputStream.flush();
		outputStream.close();
		return base64Img;
	}

	/**
	 * 绘制圆弧形文字
	 *
	 * @param g2d          画笔
	 * @param circleRadius 半径
	 * @param font         字
	 * @param isTop        置顶
	 */
	private static void drawArcFont4Circle(Graphics2D g2d, int circleRadius, SealFont font, boolean isTop) {
		if (font == null) {
			return;
		}

		//1.字体长度
		int textLen = font.getText().length();

		//2.字体大小，默认根据字体长度动态设定
		int size = font.getSize() == null ? (55 - textLen * 2) : font.getSize();

		//3.字体样式
		int style = font.getBold() ? Font.BOLD : Font.PLAIN;

		//4.构造字体
		Font f = FontBuilder.getAwtFont(font.getFamily(), style, size);

		FontRenderContext context = g2d.getFontRenderContext();
		Rectangle2D rectangle = f.getStringBounds(font.getText(), context);

		//5.文字之间间距，默认动态调整
		double space;
		if (font.getSpace() != null) {
			space = font.getSpace();
		} else {
			if (textLen == 1) {
				space = 0;
			} else {
				space = rectangle.getWidth() / (textLen - 1) * 0.9;
			}
		}

		//6.距离外圈距离
		int margin = font.getMargin() == null ? INIT_BEGIN : font.getMargin();

		//7.写字
		double newRadius = circleRadius + rectangle.getY() - margin;
		double radianPerInterval = 2 * Math.asin(space / (2 * newRadius));

		double fix = 0.04;
		if (isTop) {
			fix = 0.18;
		}
		double firstAngle;
		if (!isTop) {
			if (textLen % INT_TWO == 1) {
				firstAngle = Math.PI + Math.PI / 2 - (textLen - 1) * radianPerInterval / 2.0 - fix;
			} else {
				firstAngle = Math.PI + Math.PI / 2 - ((textLen / 2.0 - 0.5) * radianPerInterval) - fix;
			}
		} else {
			if (textLen % INT_TWO == 1) {
				firstAngle = (textLen - 1) * radianPerInterval / 2.0 + Math.PI / 2 + fix;
			} else {
				firstAngle = (textLen / 2.0 - 0.5) * radianPerInterval + Math.PI / 2 + fix;
			}
		}

		for (int i = 0; i < textLen; i++) {
			double theta;
			double thetaX;
			double thetaY;

			if (!isTop) {
				theta = firstAngle + i * radianPerInterval;
				thetaX = newRadius * Math.sin(Math.PI / 2 - theta);
				thetaY = newRadius * Math.cos(theta - Math.PI / 2);
			} else {
				theta = firstAngle - i * radianPerInterval;
				thetaX = newRadius * Math.sin(Math.PI / 2 - theta);
				thetaY = newRadius * Math.cos(theta - Math.PI / 2);
			}

			AffineTransform transform;
			if (!isTop) {
				transform = AffineTransform.getRotateInstance(Math.PI + Math.PI / 2 - theta);
			} else {
				transform = AffineTransform.getRotateInstance(Math.PI / 2 - theta + Math.toRadians(8));
			}
			Font f2 = f.deriveFont(transform);
			g2d.setFont(f2);
			g2d.drawString(font.getText().substring(i, i + 1), (float) (circleRadius + thetaX + INIT_BEGIN), (float) (circleRadius - thetaY + INIT_BEGIN));
		}
	}

	/**
	 * 绘制椭圆弧形文字
	 *
	 * @param g2d        画笔
	 * @param sealCircle 圆
	 * @param font       字
	 * @param isTop      置顶
	 */
	private static void drawArcFont4Oval(Graphics2D g2d, SealCircle sealCircle, SealFont font, boolean isTop) {
		if (font == null) {
			return;
		}
		float radiusX = sealCircle.getWidth();
		float radiusY = sealCircle.getHeight();
		float radiusWidth = radiusX + sealCircle.getLine();
		float radiusHeight = radiusY + sealCircle.getLine();

		//1.字体长度
		int textLen = font.getText().length();

		//2.字体大小，默认根据字体长度动态设定
		int size = font.getSize() == null ? 25 + (10 - textLen) / 2 : font.getSize();

		//3.字体样式
		int style = font.getBold() ? Font.BOLD : Font.PLAIN;

		//4.构造字体
		Font f = FontBuilder.getAwtFont(font.getFamily(), style, size);

		//5.总的角跨度
		double totalArcAng = font.getSpace() * textLen;

		//6.从边线向中心的移动因子
		float minRat = 0.90f;

		double startAngle = isTop ? -90f - totalArcAng / 2f : 90f - totalArcAng / 2f;
		double step = 0.5;
		int alCount = (int) Math.ceil(totalArcAng / step) + 1;
		double[] angleArr = new double[alCount];
		double[] arcLenArr = new double[alCount];
		int num = 0;
		double accArcLen = 0.0;
		angleArr[num] = startAngle;
		arcLenArr[num] = accArcLen;
		num++;
		double angR = startAngle * Math.PI / 180.0;
		double lastX = radiusX * Math.cos(angR) + radiusWidth;
		double lastY = radiusY * Math.sin(angR) + radiusHeight;
		for (double i = startAngle + step; num < alCount; i += step) {
			angR = i * Math.PI / 180.0;
			double x = radiusX * Math.cos(angR) + radiusWidth, y = radiusY * Math.sin(angR) + radiusHeight;
			accArcLen += Math.sqrt((lastX - x) * (lastX - x) + (lastY - y) * (lastY - y));
			angleArr[num] = i;
			arcLenArr[num] = accArcLen;
			lastX = x;
			lastY = y;
			num++;
		}
		double arcPer = accArcLen / textLen;
		for (int i = 0; i < textLen; i++) {
			double arcL = i * arcPer + arcPer / 2.0;
			double ang = 0.0;
			for (int p = 0; p < arcLenArr.length - 1; p++) {
				if (arcLenArr[p] <= arcL && arcL <= arcLenArr[p + 1]) {
					ang = (arcL >= ((arcLenArr[p] + arcLenArr[p + 1]) / 2.0)) ? angleArr[p + 1] : angleArr[p];
					break;
				}
			}
			angR = (ang * Math.PI / 180f);
			Float x = radiusX * (float) Math.cos(angR) + radiusWidth;
			Float y = radiusY * (float) Math.sin(angR) + radiusHeight;
			double qxang = Math.atan2(radiusY * Math.cos(angR), -radiusX * Math.sin(angR));
			double fxang = qxang + Math.PI / 2.0;

			int subIndex = isTop ? i : textLen - 1 - i;
			String c = font.getText().substring(subIndex, subIndex + 1);

			//获取文字高宽
			FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
			Rectangle rectangle = f.getStringBounds(c, frc).getBounds();
			float h = (float) rectangle.getHeight();
			float w = (float) rectangle.getWidth();

			if (isTop) {
				x += h * minRat * (float) Math.cos(fxang);
				y += h * minRat * (float) Math.sin(fxang);
				x += -w / 2f * (float) Math.cos(qxang);
				y += -w / 2f * (float) Math.sin(qxang);
			} else {
				x += (h * minRat) * (float) Math.cos(fxang);
				y += (h * minRat) * (float) Math.sin(fxang);
				x += w / 2f * (float) Math.cos(qxang);
				y += w / 2f * (float) Math.sin(qxang);
			}

			// 旋转
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.scale(0.8, 1);
			if (isTop) {
				affineTransform.rotate(Math.toRadians((fxang * 180.0 / Math.PI - 90)), 0, 0);
			} else {
				affineTransform.rotate(Math.toRadians((fxang * 180.0 / Math.PI + 180 - 90)), 0, 0);
			}
			Font f2 = f.deriveFont(affineTransform);
			g2d.setFont(f2);
			g2d.drawString(c, x.intValue() + INIT_BEGIN, y.intValue() + INIT_BEGIN);
		}
	}

	/**
	 * 画文字
	 */
	private static void drawFont(Graphics2D g2d, int circleWidth, int circleHeight, SealFont font) {
		// 构造字体
		Font f = structureFont(font);
		if (f == null) {
			return;
		}

		g2d.setFont(f);

		FontRenderContext context = g2d.getFontRenderContext();
		Rectangle2D rectangle2D = f.getStringBounds(font.getText(), context);
		//5.设置上边距，默认在中心
		float margin = font.getMargin() == null ?
				(float) (circleHeight / 2 - rectangle2D.getCenterY()) :
				(float) (circleHeight / 2 - rectangle2D.getCenterY()) + (float) font.getMargin();
		g2d.drawString(font.getText(), (float) (circleWidth / 2 - rectangle2D.getCenterX()), margin);

	}

	/**
	 * 画圆
	 */
	private static void drawCircle(Graphics2D g2d, SealCircle circle, int x, int y) {
		if (circle == null) {
			return;
		}

		//1.圆线条粗细默认是圆直径的1/35
		int lineSize = circle.getLine() == null ? circle.getHeight() * 2 / (35) : circle.getLine();

		//2.画圆
		g2d.setStroke(new BasicStroke(lineSize));
		g2d.drawOval(x, y, circle.getWidth() * 2, circle.getHeight() * 2);
	}

	/**
	 * 生成字体
	 *
	 * @param font 章-字体参数
	 * @return 字体
	 */
	private static Font structureFont(SealFont font) {
		if (font == null) {
			return null;
		}

		//1.字体长度
		int textLen = font.getText().length();

		//2.字体大小，默认根据字体长度动态设定
		int size = font.getSize() == null ? (55 - textLen * 2) : font.getSize();

		//3.字体样式
		int style = font.getBold() ? Font.BOLD : Font.PLAIN;

		//4.构造字体
		return FontBuilder.getAwtFont(font.getFamily(), style, size);
	}
}
