package com.xjinyao.report.core.chart.axes.impl;

import com.xjinyao.report.core.chart.axes.BaseAxes;
import com.xjinyao.report.core.chart.axes.ScaleLabel;
import com.xjinyao.report.core.chart.axes.XPosition;

/**
 * @author 谢进伟
 * @since 2023年03月01日
 */
public class XAxes extends BaseAxes {
	private XPosition xposition = XPosition.bottom;

	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"ticks\":{");
		sb.append("\"minRotation\":" + getRotation() + "");
		sb.append("}");
		ScaleLabel scaleLabel = getScaleLabel();
		if (scaleLabel != null) {
			sb.append(",\"scaleLabel\":" + scaleLabel.toJson());
		}
		sb.append("}");
		return sb.toString();
	}

	public XPosition getXposition() {
		return xposition;
	}

	public void setXposition(XPosition xposition) {
		this.xposition = xposition;
	}
}
