package com.xjinyao.report.core.export.pdf.font;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.exception.ReportException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.*;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
@Slf4j
public class FontBuilder implements ApplicationContextAware {
	public static final Map<String, String> fontPathMap = new HashMap<>();
	private static final Map<String, BaseFont> fontMap = new HashMap<>();
	private static ApplicationContext applicationContext;
	private static List<String> systemFontNameList = new ArrayList<>();

	private boolean loadSystemFont = false;

	/**
	 * 获得字体
	 *
	 * @param fontName   字体名字
	 * @param fontSize   字体大小
	 * @param fontBold   字体加粗
	 * @param fontItalic 字体斜体
	 * @param underLine  下划线
	 * @return {@link Font}
	 */
	public static Font getFont(String fontName, int fontSize, boolean fontBold, boolean fontItalic, boolean underLine) {
		BaseFont baseFont = fontMap.get(fontName);
		Font font = null;
		if (baseFont != null) {
			font = new Font(baseFont);
		} else {
			font = FontFactory.getFont(fontName);
		}
		font.setSize(fontSize);
		int fontStyle = Font.NORMAL;
		if (fontBold && fontItalic && underLine) {
			fontStyle = Font.BOLD | Font.ITALIC | Font.UNDERLINE;
		} else if (fontBold) {
			if (fontItalic) {
				fontStyle = Font.BOLD | Font.ITALIC;
			} else if (underLine) {
				fontStyle = Font.BOLD | Font.UNDERLINE;
			} else {
				fontStyle = Font.BOLD;
			}
		} else if (fontItalic) {
			if (underLine) {
				fontStyle = Font.ITALIC | Font.UNDERLINE;
			} else if (fontBold) {
				fontStyle = Font.ITALIC | Font.BOLD;
			} else {
				fontStyle = Font.ITALIC;
			}
		} else if (underLine) {
			fontStyle = Font.UNDERLINE;
		}
		font.setStyle(fontStyle);
		return font;
	}

	/**
	 * 得到awt字体
	 *
	 * @param fontName  字体名字
	 * @param fontStyle 字体样式
	 * @param size      大小
	 * @return {@link java.awt.Font}
	 */
	public static java.awt.Font getAwtFont(String fontName, int fontStyle, float size) {
		if (systemFontNameList.contains(fontName)) {
			return new java.awt.Font(fontName, fontStyle, new Float(size).intValue());
		}
		String fontPath = fontPathMap.get(fontName);
		if (fontPath == null) {
			fontName = "宋体";
			fontPath = fontPathMap.get(fontName);
			if (fontPath == null) {
				return null;
			}
		}
		InputStream inputStream = null;
		try {
			inputStream = applicationContext.getResource(fontPath).getInputStream();
			java.awt.Font font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, inputStream);
			return font.deriveFont(fontStyle, size);
		} catch (Exception e) {
			throw new ReportException(e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	public void setLoadSystemFont(boolean loadSystemFont) {
		this.loadSystemFont = loadSystemFont;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		FontBuilder.applicationContext = applicationContext;
		if (loadSystemFont) {
			try {
				GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
				if (environment != null) {
					String[] fontNames = environment.getAvailableFontFamilyNames();
					systemFontNameList.addAll(Arrays.asList(fontNames));
				}
			} catch (Exception e) {
				//ignore exception
				log.warn("Unable to load system font");
			}
		}
		Collection<FontRegister> fontRegisters = applicationContext.getBeansOfType(FontRegister.class).values();
		for (FontRegister fontReg : fontRegisters) {
			String fontName = fontReg.getFontName();
			String fontPath = fontReg.getFontPath();
			if (StringUtils.isEmpty(fontPath) || StringUtils.isEmpty(fontName)) {
				continue;
			}
			try {
				BaseFont baseFont = getIdentityFont(fontName, fontPath, applicationContext);
				if (baseFont == null) {
					throw new ReportComputeException("Font " + fontPath + " does not exist");
				}
				fontMap.put(fontName, baseFont);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ReportComputeException(e);
			}
		}
	}

	private BaseFont getIdentityFont(String fontFamily, String fontPath, ApplicationContext applicationContext) throws DocumentException, IOException {
		if (!fontPath.startsWith(ApplicationContext.CLASSPATH_URL_PREFIX)) {
			fontPath = ApplicationContext.CLASSPATH_URL_PREFIX + fontPath;
		}
		String fontName = fontPath;
		int lastSlashPos = fontPath.lastIndexOf("/");
		if (lastSlashPos != -1) {
			fontName = fontPath.substring(lastSlashPos + 1, fontPath.length());
		}
		if (fontName.toLowerCase().endsWith(".ttc")) {
			fontName = fontName + ",0";
		}
		InputStream inputStream = null;
		try {
			fontPathMap.put(fontFamily, fontPath);
			inputStream = applicationContext.getResource(fontPath).getInputStream();
			byte[] bytes = IOUtils.toByteArray(inputStream);
			BaseFont baseFont = BaseFont.createFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, bytes, null);
			baseFont.setSubset(true);
			return baseFont;
		} finally {
			if (inputStream != null) inputStream.close();
		}
	}
}
