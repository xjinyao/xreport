package com.xjinyao.report.core.chart;

import com.xjinyao.report.core.cache.CacheUtils;
import com.xjinyao.report.core.model.Cell;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class ChartData {
	private String id;
	private String json;
	@JsonIgnore
	private String base64Data;
	@JsonIgnore
	private int width;
	@JsonIgnore
	private int height;

	public ChartData(String json, Cell cell) {
		this.json = json;
		this.id = cell.getName();
	}

	public String getJson() {
		return json;
	}

	public void setBase64Data(String base64Data) {
		this.base64Data = base64Data;
	}

	public String retriveBase64Data() {
		if (base64Data != null) {
			return base64Data;
		}
		ChartData data = CacheUtils.getChartData(id);
		if (data != null) {
			return data.retriveBase64Data();
		}
		return base64Data;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getId() {
		return id;
	}
}
