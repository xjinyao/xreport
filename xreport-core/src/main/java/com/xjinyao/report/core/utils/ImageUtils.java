package com.xjinyao.report.core.utils;

import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.image.ChartImageProcessor;
import com.xjinyao.report.core.image.ImageProcessor;
import com.xjinyao.report.core.image.ImageType;
import com.xjinyao.report.core.image.StaticImageProcessor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ImageUtils {
	private static final String BASE64_SEPARATOR = "base64,";
	private static final String BASE64_IMG_DATA_REG = "^data:image/(png|jpeg|jpg);" + BASE64_SEPARATOR + ".+";
	private static Map<ImageType, ImageProcessor<?>> imageProcessorMap = new HashMap<>();

	static {
		StaticImageProcessor staticImageProcessor = new StaticImageProcessor();
		imageProcessorMap.put(ImageType.image, staticImageProcessor);
		ChartImageProcessor chartImageProcessor = new ChartImageProcessor();
		imageProcessorMap.put(ImageType.chart, chartImageProcessor);
	}

	public static InputStream base64DataToInputStream(String base64Data) {
		byte[] bytes = Base64Utils.decodeFromString(base64Data);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		return inputStream;
	}

	@SuppressWarnings("unchecked")
	public static String getImageBase64Data(ImageType type, Object data, int width, int height) {
		ImageProcessor<Object> targetProcessor = (ImageProcessor<Object>) imageProcessorMap.get(type);
		if (targetProcessor == null) {
			throw new ReportComputeException("Unknow image type :" + type);
		}
		InputStream inputStream = targetProcessor.getImage(data);
		try {
			String str = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			if (str.matches(ImageUtils.BASE64_IMG_DATA_REG)) {
				return StringUtils.substringAfter(str, BASE64_SEPARATOR);
			}

			if (width > 0 && height > 0) {
				BufferedImage inputImage = ImageIO.read(inputStream);
				BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
				Graphics2D g = outputImage.createGraphics();
				g.drawImage(inputImage, 0, 0, width, height, null);
				g.dispose();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ImageIO.write(outputImage, "png", outputStream);
				inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			}
			byte[] bytes = IOUtils.toByteArray(inputStream);
			return Base64Utils.encodeToString(bytes);
		} catch (Exception ex) {
			throw new ReportComputeException(ex);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
}
