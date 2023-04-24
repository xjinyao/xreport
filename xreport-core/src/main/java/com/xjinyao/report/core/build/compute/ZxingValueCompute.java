package com.xjinyao.report.core.build.compute;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.xjinyao.report.core.build.BindData;
import com.xjinyao.report.core.build.Context;
import com.xjinyao.report.core.definition.value.Source;
import com.xjinyao.report.core.definition.value.ValueType;
import com.xjinyao.report.core.definition.value.ZxingValue;
import com.xjinyao.report.core.exception.ReportComputeException;
import com.xjinyao.report.core.expression.model.Expression;
import com.xjinyao.report.core.expression.model.data.BindDataListExpressionData;
import com.xjinyao.report.core.expression.model.data.ExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectExpressionData;
import com.xjinyao.report.core.expression.model.data.ObjectListExpressionData;
import com.xjinyao.report.core.model.Cell;
import com.xjinyao.report.core.model.Image;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ZxingValueCompute implements ValueCompute {
	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xFFFFFFFF;

	@Override
	public List<BindData> compute(Cell cell, Context context) {
		List<BindData> list = new ArrayList<>();
		ZxingValue value = (ZxingValue) cell.getValue();
		String format = value.getFormat();
		BarcodeFormat barcodeForamt = BarcodeFormat.QR_CODE;
		if (StringUtils.isNotBlank(format)) {
			barcodeForamt = BarcodeFormat.valueOf(format);
		}
		int w = value.getWidth();
		int h = value.getHeight();
		Source source = value.getSource();
		if (source.equals(Source.text)) {
			String data = value.getValue();
			Image image = buildImage(barcodeForamt, data, w, h);
			list.add(new BindData(image));
		} else {
			Expression expression = value.getExpression();
			ExpressionData<?> data = expression.execute(cell, cell, context);
			if (data instanceof BindDataListExpressionData) {
				BindDataListExpressionData listData = (BindDataListExpressionData) data;
				List<BindData> bindDataList = listData.getData();
				for (BindData bindData : bindDataList) {
					Object obj = bindData.getValue();
					if (obj == null) obj = "";
					Image image = buildImage(barcodeForamt, obj.toString(), w, h);
					list.add(new BindData(image));
				}
			} else if (data instanceof ObjectExpressionData) {
				ObjectExpressionData exprData = (ObjectExpressionData) data;
				Object obj = exprData.getData();
				if (obj == null) {
					obj = "";
				} else if (obj instanceof String) {
					String text = obj.toString();
					if (text.startsWith("\"") && text.endsWith("\"")) {
						text = text.substring(1, text.length() - 1);
					}
					obj = text;
				}
				Image image = buildImage(barcodeForamt, obj.toString(), w, h);
				list.add(new BindData(image));
			} else if (data instanceof ObjectListExpressionData) {
				ObjectListExpressionData listExprData = (ObjectListExpressionData) data;
				List<?> listData = listExprData.getData();
				for (Object obj : listData) {
					if (obj == null) {
						obj = "";
					} else if (obj instanceof String) {
						String text = obj.toString();
						if (text.startsWith("\"") && text.endsWith("\"")) {
							text = text.substring(1, text.length() - 1);
						}
						obj = text;
					}
					Image image = buildImage(barcodeForamt, obj.toString(), w, h);
					list.add(new BindData(image));
				}
			}
		}
		return list;
	}

	private Image buildImage(BarcodeFormat format, String data, int w, int h) {
		try {
			Map<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			hints.put(EncodeHintType.MARGIN, 0);
			if (format.equals(BarcodeFormat.QR_CODE)) {
				hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			}
			BitMatrix matrix = new MultiFormatWriter().encode(data, format, w, h, hints);
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
				}
			}
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", outputStream);
			byte[] bytes = outputStream.toByteArray();
			String base64Data = Base64Utils.encodeToString(bytes);
			IOUtils.closeQuietly(outputStream);
			return new Image(base64Data, w, h);
		} catch (Exception ex) {
			throw new ReportComputeException(ex);
		}
	}

	@Override
	public ValueType type() {
		return ValueType.zxing;
	}
}
